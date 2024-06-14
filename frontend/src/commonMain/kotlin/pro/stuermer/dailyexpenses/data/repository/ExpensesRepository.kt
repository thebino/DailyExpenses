package pro.stuermer.dailyexpenses.data.repository

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.NetworkResource
import pro.stuermer.dailyexpenses.data.network.SyncStatus
import pro.stuermer.dailyexpenses.shared.Expense

interface ExpensesRepository : KoinComponent {
    suspend fun getExpensesForDate(date: LocalDate): Flow<NetworkResource<List<Expense>>>
    suspend fun getExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun sync(): SyncStatus
}

class ExpensesRepositoryImpl : ExpensesRepository {
    private val expensesApi: ExpensesApi by inject()
    private val dao: ExpensesDao by inject()

    override suspend fun addExpense(expense: DomainExpense) {
        dao.insert(expense.toPersistenceModel().copy(identifier = UUID.randomUUID().toString()))
    }

    override suspend fun updateExpense(expense: Expense) {
        dao.update(expense.copy(updatedDate = LocalDateTime.now()).toPersistenceModel())
    }

    override suspend fun deleteExpense(expense: Expense) {
        val tmpExpense = expense.copy(deletedDate = LocalDateTime.now())
        val persistedExpense = tmpExpense.toPersistenceModel()
        dao.update(persistedExpense)
    }

    /**
     * Returns all expenses even deleted ones.
     */
    override suspend fun getExpenses(): Flow<List<DomainExpense>> =
        dao.getAllExpenses().map { expenses: List<PersistedExpense> ->
            expenses.filter { expense ->
                expense.deletedDate == null
            }.map { expense ->
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
        ).map { expenses: List<PersistedExpense> ->
            Resource.Success(
                data = expenses.filter { expense ->
                    expense.deletedDate == null
                }.map { expense ->
                    expense.toDomainModel()
                }.reversed()
            )
        }.onStart {
            Resource.Loading
        }.catch {
            Resource.Error<Resource<List<DomainExpense>>>(it)
        }

    /**
     * Synchronize with a remote source
     */
    override suspend fun sync(): SyncStatus {
        Timber.i("+++ sync +++")
        val sharing = sharingDao.getSharings().firstOrNull()

        if (sharing.isNullOrEmpty()) {
            Timber.i("No sharing group found! Skip sync")
            return SyncStatus.SyncSkipped
        }

        val sharingGroup = sharing[0].code

        // upload ALL local expenses to remote datasource
        if (!uploadLocalExpenses(sharingGroup = sharingGroup)) {
            return SyncStatus.SyncFailed(message = "upload local expenses failed!")
        }

        // download expenses from remote datasource (excl. deleted)
        if (!downloadRemoteExpenses(sharingGroup = sharingGroup)) {
            return SyncStatus.SyncFailed(message = "download remote expenses failed")
        }

        return SyncStatus.SyncSucceeded
    }

    private suspend fun uploadLocalExpenses(sharingGroup: String): Boolean {
        val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
        val localExpenses: List<NetworkExpense> = persistedExpenses.map { it.toNetworkExpense() }
        val uploadResponse = api.addExpenses(
            code = sharingGroup, expenses = localExpenses
        )

        uploadResponse.onSuccess {
            Timber.i("+ uploaded   ${localExpenses.size} expenses to remote.")
        }

        uploadResponse.onFailure {
            Timber.w(it, "! could not upload expenses to remote datasource.")
            return false
        }
        return true
    }

    @Suppress("MaximumLineLength", "MaxLineLength", "MaxLineLength", "SpreadOperator")
    private suspend fun downloadRemoteExpenses(sharingGroup: String): Boolean {
        val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
        val onlineExpenses = api.getExpenses(sharingGroup)
        onlineExpenses.onSuccess { expenses ->
            Timber.i("+ downloaded ${expenses.size} expenses from remote.")
            for (remoteExpense in expenses) {
                val localExpense =
                    persistedExpenses.firstOrNull { it.identifier == remoteExpense.id }

                // delete local if it was deleted remote
                if (remoteExpense.deletedDate != null) {
                    dao.delete(listOf(remoteExpense.id))
                }

                // create/update local or remote
                when {
                    localExpense == null -> {
                        // create local if it doesn't exist
                        dao.insert(remoteExpense.toPersistenceExpense())
                    }

                    localExpense.updatedDate == null && remoteExpense.updatedDate != null -> {
                        // local has never been updated before, but remote has
                        dao.insert(remoteExpense.toPersistenceExpense())
                    }

                    localExpense.updatedDate != null && remoteExpense.updatedDate != null && localExpense.updatedDate < remoteExpense.updatedDate -> {
                        // local has been updated before, but remote is newer
                        dao.insert(remoteExpense.toPersistenceExpense())
                    }

                    localExpense.updatedDate != null && remoteExpense.updatedDate != null && localExpense.updatedDate > remoteExpense.updatedDate -> {
                        // local update is newer, update remote
                        api.addExpenses(
                            code = sharingGroup, expenses = listOf(localExpense.toNetworkExpense())
                        )
                    }

                    else -> {
                        // already up-to-date
                    }
                }
            }

            // save network data in cache
            dao.insert(
                *expenses.map { expense ->
                    expense.toPersistenceExpense()
                }.toTypedArray()
            )
        }

        onlineExpenses.onFailure {
            Timber.w(it, "Could not download expenses from remote datasource")
            return false
        }

        return true
    }
}
