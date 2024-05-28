package pro.stuermer.dailyexpenses.domain.usecase

import pro.stuermer.dailyexpenses.data.repository.SharingsRepository

class JoinSharingUseCase(
    val repository: SharingsRepository
) {
    suspend operator fun invoke(code: String) = repository.joinSharing(code)
}
