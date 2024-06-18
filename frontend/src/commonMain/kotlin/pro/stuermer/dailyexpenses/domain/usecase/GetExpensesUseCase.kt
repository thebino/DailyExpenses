package pro.stuermer.dailyexpenses.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.domain.Expense

class GetExpensesUseCase(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(): Flow<List<Expense>> = repository.getExpenses()
}
