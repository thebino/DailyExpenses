package pro.stuermer.dailyexpenses.data.repository

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.ExpenseMapper
import pro.stuermer.dailyexpenses.data.ExpenseMapper.formatter
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.NetworkResource
import pro.stuermer.dailyexpenses.data.network.SyncStatus
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.shared.Expense
import pro.stuermer.dailyexpenses.data.persistence.ExpenseEntity as PersistedExpense
import pro.stuermer.dailyexpenses.domain.Expense as DomainExpense
import pro.stuermer.dailyexpenses.shared.Expense as NetworkExpense

interface ExpensesRepository : KoinComponent {
    suspend fun getExpensesForDate(date: LocalDate): Flow<NetworkResource<List<DomainExpense>>>
    suspend fun getExpenses(): Flow<List<DomainExpense>>
    suspend fun addExpense(expense: DomainExpense)
    suspend fun updateExpense(expense: DomainExpense)
    suspend fun deleteExpense(expense: DomainExpense)
    suspend fun sync(): SyncStatus

    class Default : ExpensesRepository {
        private val api: ExpensesApi by inject()
        private val dao: ExpensesDao by inject()
        private val sharingDao: SharingDao by inject()

        override suspend fun addExpense(expense: DomainExpense) {
            dao.insert(
                ExpenseMapper.toPersistenceModel(expense)
                    .copy(identifier = UUID.randomUUID().toString())
            )
        }

        override suspend fun updateExpense(expense: DomainExpense) {
            dao.update(
                ExpenseMapper.toPersistenceModel(expense).copy(
                    updatedDate = LocalDateTime.now().format(formatter)
                )
            )
        }

        override suspend fun deleteExpense(expense: DomainExpense) {
            val tmpExpense = expense.copy(deletedDate = LocalDateTime.now())
            val persistedExpense = ExpenseMapper.toPersistenceModel(tmpExpense)
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
                    ExpenseMapper.toDomain(expense)
                }.reversed()
            }

        /**
         * Returns all available expenses for a given month, but deleted ones.
         */
        override suspend fun getExpensesForDate(date: LocalDate): Flow<NetworkResource<List<DomainExpense>>> =
            dao.getExpensesForDate(
                fromDate = LocalDate.of(date.year, date.month, 1).format(formatter),
                toDate = LocalDate.of(date.year, date.month, 1)
                    .plusDays(LocalDate.of(date.year, date.month, 1).lengthOfMonth() - 1L)
                    .format(formatter)
            ).map { expenses: List<PersistedExpense> ->
                NetworkResource.Success(data = expenses.filter { expense ->
                    expense.deletedDate == null
                }.map { expense ->
                    ExpenseMapper.toDomain(expense)
                }.reversed()
                )
            }.onStart {
                NetworkResource.Loading
            }.catch {
                NetworkResource.Error<NetworkResource<List<DomainExpense>>>(it)
            }

        /**
         * Synchronize with a remote source
         */
        override suspend fun sync(): SyncStatus {
//            Timber.i("+++ sync +++")
            val sharing = sharingDao.getSharings().firstOrNull()

            if (sharing.isNullOrEmpty()) {
//                Timber.i("No sharing group found! Skip sync")
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
            val localExpenses: List<NetworkExpense> =
                persistedExpenses.map { ExpenseMapper.toNetwork(it) }
            val uploadResponse = api.addExpenses(
                code = sharingGroup, expenses = localExpenses
            )

            uploadResponse.onSuccess {
//            Timber.i("+ uploaded   ${localExpenses.size} expenses to remote.")
            }

            uploadResponse.onFailure {
//            Timber.w(it, "! could not upload expenses to remote datasource.")
                return false
            }
            return true
        }

        @Suppress("MaximumLineLength", "MaxLineLength", "MaxLineLength", "SpreadOperator", "CyclomaticComplexMethod")
        private suspend fun downloadRemoteExpenses(sharingGroup: String): Boolean {
            val persistedExpenses: List<PersistedExpense> = dao.getAllExpenses().first()
            val onlineExpenses = api.getExpenses(sharingGroup)
            onlineExpenses.onSuccess { expenses ->
//            Timber.i("+ downloaded ${expenses.size} expenses from remote.")
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
                            dao.insert(ExpenseMapper.toPersistenceModel(remoteExpense))
                        }

                        localExpense.updatedDate == null && remoteExpense.updatedDate != null -> {
                            // local has never been updated before, but remote has
                            dao.insert(ExpenseMapper.toPersistenceModel(remoteExpense))
                        }

                        localExpense.updatedDate != null && remoteExpense.updatedDate != null && LocalDateTime.parse(localExpense.updatedDate, formatter) < remoteExpense.updatedDate?.let {
                            LocalDateTime.parse(
                                it,
                                formatter
                            )
                        } -> {
                            // local has been updated before, but remote is newer
                            dao.insert(ExpenseMapper.toPersistenceModel(remoteExpense))
                        }

                        localExpense.updatedDate != null && remoteExpense.updatedDate != null && LocalDateTime.parse(localExpense.updatedDate, formatter) > remoteExpense.updatedDate?.let {
                            LocalDateTime.parse(
                                it,
                                formatter
                            )
                        } -> {
                            // local update is newer, update remote
                            api.addExpenses(
                                code = sharingGroup,
                                expenses = listOf(ExpenseMapper.toNetwork(localExpense))
                            )
                        }

                        else -> {
                            // already up-to-date
                        }
                    }
                }

                // save network data in cache
                dao.insert(
                    *expenses.map { expense: Expense ->
                        ExpenseMapper.toPersistenceModel(expense)
                    }.toTypedArray()
                )
            }

            onlineExpenses.onFailure {
//            Timber.w(it, "Could not download expenses from remote datasource")
                return false
            }

            return true
        }
    }
}
