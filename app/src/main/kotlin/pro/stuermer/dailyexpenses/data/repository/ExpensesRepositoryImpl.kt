package pro.stuermer.dailyexpenses.data.repository

import io.ktor.utils.io.printStack
import java.time.LocalDate
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.domain.model.Expense
import timber.log.Timber
import pro.stuermer.dailyexpenses.data.persistence.model.Expense as PersistedExpense
import pro.stuermer.dailyexpenses.domain.model.Expense as DomainExpense
import pro.stuermer.dailyexpenses.data.model.Expense as NetworkExpense

class ExpensesRepositoryImpl(
    private val api: ExpensesApi,
    private val dao: ExpensesDao,
    private val sharingDao: SharingDao
) : ExpensesRepository {
    override suspend fun addExpense(expense: DomainExpense) {
        dao.insert(expense.toPersistenceModel().copy(identifier = UUID.randomUUID().toString()))
    }

    override suspend fun updateExpense(expense: Expense) {
        dao.update(expense.toPersistenceModel())
    }

    override suspend fun deleteExpense(expense: Expense) {
        val tmpExpense = expense.copy(deletedDate = LocalDate.now())
        val persistedExpense = tmpExpense.toPersistenceModel()
        dao.update(persistedExpense)
    }

    /**
     * Returns all expenses even deleted ones.
     */
    override suspend fun getExpenses(): Flow<List<DomainExpense>> = dao.getAllExpenses().map {
        it.map { expense ->
            expense.toDomainModel()
        }.reversed()
    }

    /**
     * Returns all available expenses for a given month, but deleted ones.
     */
    override suspend fun getExpensesForDate(date: LocalDate): Flow<Resource<List<DomainExpense>>> =
        dao.getExpensesForDate(
            fromDate = LocalDate.of(date.year, date.month, 1),
            toDate = LocalDate.of(date.year, date.month, 1)
                .plusDays(LocalDate.of(date.year, date.month, 1).lengthOfMonth() - 1L)
        ).map {
            Resource.Success(it.filter { expense ->
                expense.deletedDate == null
            }.map { expense ->
                expense.toDomainModel()
            }.reversed())
        }.onStart {
            Resource.Loading
        }.catch {
            Resource.Error<Resource<List<DomainExpense>>>(it)
        }

    /**
     * Synchronize with a remote source
     */
    override suspend fun sync(): Boolean {
        Timber.i("+++ sync +++")
        var result: Boolean = true
        val sharing = sharingDao.getSharings().firstOrNull()

        if (sharing.isNullOrEmpty()) {
            Timber.e("No sharing group found! Skip sync")
            return false
        }

        val sharingGroup = sharing[0].code

        // upload local expenses to remote datasource
        if (!uploadLocalExpenses(sharingGroup = sharingGroup)) {
            result = false
        }

        // delete local expenses in remote datasource
        if (!deleteLocalExpenses(sharingGroup = sharingGroup)) {
            result = false
        }


        // download expenses from remote datasource
        if (!downloadRemoteExpenses(sharingGroup = sharingGroup)) {
            result = false
        }

        return result
    }

    private suspend fun uploadLocalExpenses(sharingGroup: String): Boolean {
        val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
        val localExpenses: List<NetworkExpense> =
            persistedExpenses.filter { it.deletedDate == null }.map { it.toNetworkExpense() }
        val uploadResponse = api.addExpenses(
            code = sharingGroup,
            expenses = localExpenses
        )

        uploadResponse.onSuccess {
            Timber.i("+ uploaded ${localExpenses.size} expenses to remote datasource")
        }

        uploadResponse.onFailure {
            Timber.w( it, "! could not upload expenses to remote datasource.")
            return false
        }
        return true
    }

    /**
     * delete local expenses in remote datasource
     */
    private suspend fun deleteLocalExpenses(sharingGroup: String): Boolean {
        val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
        val localDeletedIds: List<String> =
            persistedExpenses.filter { it.deletedDate != null }.map { it.identifier }

        Timber.i("+ localDeletedIds=$localDeletedIds")

        val deletedResponse = api.deleteIds(
            code = sharingGroup,
            localDeletedIds = localDeletedIds
        )
        deletedResponse.onSuccess {
            Timber.i("+ deleted ${localDeletedIds.size} expenses in remote datasource")
            dao.delete(localDeletedIds)
        }
        deletedResponse.onFailure {
            Timber.w(it, "! could not delete local expenses in remote datasource.")
            return false
        }
        return true
    }

    private suspend fun downloadRemoteExpenses(sharingGroup: String): Boolean {
        val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
        val onlineExpenses = api.getExpenses(sharingGroup)
        onlineExpenses.onSuccess { expenses ->
            Timber.i("+ onlineExpenses=${expenses.size}")
            for (remoteExpense in expenses) {
                // delete local if it was deleted remote
                if (remoteExpense.deletedDate != null) {
                    dao.delete(listOf(remoteExpense.id))
                }

                val localExpense = persistedExpenses.first { it.identifier == remoteExpense.id }
                if (localExpense.updatedDate != null && localExpense.updatedDate < remoteExpense.updatedDate) {
                    // update local
                    dao.insert(remoteExpense.toPersistenceExpense())
                } else if (localExpense.updatedDate != null && localExpense.updatedDate > remoteExpense.updatedDate) {
                    // update remote
                    api.addExpenses(
                        code = sharingGroup,
                        expenses = listOf(localExpense.toNetworkExpense())
                    )
                } else {
                    // already up-to-date
                }
            }

            // save network data in cache
            dao.insert(*expenses.map { expense ->
                expense.toPersistenceExpense()
            }.toTypedArray())
        }

        onlineExpenses.onFailure {
            Timber.w(it, "Could not download expenses from remote datasource")
            return false
        }

        return true
    }
}
