package pro.stuermer.dailyexpenses.domain.usecase

import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.domain.model.Expense

class AddExpenseUseCase(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}
