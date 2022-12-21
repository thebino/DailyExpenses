package pro.stuermer.dailyexpenses.data.repository

import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing

class SharingsRepositoryImpl(
    private val api: ExpensesApi,
    private val dao: SharingDao,
) : SharingsRepository {
    override suspend fun getSharings(): Flow<List<Sharing>> = dao.getSharings()

    override suspend fun joinSharing(code: String): Result<Boolean> {
        val result = api.joinSharing(code)
        result.onSuccess {
            if (it) {
                dao.insert(
                    Sharing(
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
            dao.insert(Sharing(code = it))
        }

        return result
    }

    override suspend fun leaveSharing() {
        dao.deleteAll()
    }
}
