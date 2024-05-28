package pro.stuermer.dailyexpenses.domain.usecase

import pro.stuermer.dailyexpenses.data.repository.SharingsRepository

class CreateSharingUseCase(
    val repository: SharingsRepository
) {
    suspend operator fun invoke() = repository.createSharing()
}
