package pro.stuermer.dailyexpenses.data.repository

import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing

interface SharingsRepository {
    suspend fun getSharings(): Flow<List<Sharing>>
    suspend fun joinSharing(code: String): Result<Boolean>
    suspend fun createSharing(): Result<String>
    suspend fun leaveSharing()
}
