package pro.stuermer.dailyexpenses.domain.usecase

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.domain.model.Expense

class GetExpensesForDateUseCase(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(date: LocalDate): Flow<Resource<List<Expense>>> {
        return repository.getExpensesForDate(date).distinctUntilChanged()
    }
}
