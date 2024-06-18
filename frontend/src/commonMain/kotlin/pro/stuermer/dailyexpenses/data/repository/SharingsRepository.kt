package pro.stuermer.dailyexpenses.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.SharingMapper
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.data.persistence.SharingEntity
import pro.stuermer.dailyexpenses.domain.Sharing as DomainSharing

interface SharingsRepository : KoinComponent {
    suspend fun getSharings(): Flow<List<DomainSharing>>
    suspend fun joinSharing(code: String): Result<Boolean>
    suspend fun createSharing(): Result<String>
    suspend fun leaveSharing()

    class Default : SharingsRepository {
        private val api: ExpensesApi by inject()
        private val dao: SharingDao by inject()

        override suspend fun getSharings(): Flow<List<DomainSharing>> =
            dao.getSharings().map { sharings ->
                sharings.map { sharing ->
                    SharingMapper.toDomain(sharing)
                }
            }

        override suspend fun joinSharing(code: String): Result<Boolean> {
            val result = api.joinSharing(code)
            result.onSuccess {
                if (it) {
                    dao.insert(
                        SharingEntity(
                            identifier = null,
                            code = code
                        )
                    )
                }
            }

            return result
        }

        override suspend fun createSharing(): Result<String> {
            val result = api.createSharing()
            result.onSuccess {
                dao.insert(SharingEntity(code = it))
            }

            result.onFailure {
                // could not create new sharing
            }

            return result
        }

        override suspend fun leaveSharing() {
            dao.deleteAll()
        }
    }
}
