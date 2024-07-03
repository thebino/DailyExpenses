package pro.stuermer.dailyexpenses.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.LocalDate
import pro.stuermer.dailyexpenses.data.network.NetworkResource
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.domain.Expense

class GetExpensesForDateUseCase(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(date: LocalDate): Flow<NetworkResource<List<Expense>>> {
        return repository.getExpensesForDate(date).distinctUntilChanged()
    }
}
