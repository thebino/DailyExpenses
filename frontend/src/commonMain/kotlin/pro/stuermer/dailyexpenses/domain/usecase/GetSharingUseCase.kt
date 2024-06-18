package pro.stuermer.dailyexpenses.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.domain.Sharing
import pro.stuermer.dailyexpenses.data.repository.SharingsRepository

class GetSharingUseCase(
    val repository: SharingsRepository
) {
    suspend operator fun invoke(): Flow<List<Sharing>> = repository.getSharings()
}
