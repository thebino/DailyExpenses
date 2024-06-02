package pro.stuermer.dailyexpenses.backend.repository

import pro.stuermer.dailyexpenses.backend.persistence.Instance
import pro.stuermer.dailyexpenses.shared.Expense

interface DailyExpensesRepository {
    suspend fun deleteAll()
    suspend fun getExpenses(instance: String): List<Expense>
    suspend fun getExpenseForDate(instance: String, year: Int, month: Int): List<Expense>
    suspend fun addExpenses(instance: String, expenses: List<Expense>)
    suspend fun getExpenseForId(instance: String, id: String): Expense?
    suspend fun getInstances(code: String): Instance?

    @Suppress("LongParameterList")
    suspend fun addNewExpense(
        instance: String,
        id: String,
        category: String,
        expenseDate: String,
        creationDate: String,
        updatedDate: String?,
        description: String,
        amount: Float
    ): Expense?

    suspend fun updateExpenseWithId(instance: String, expense: Expense): Int
    suspend fun deleteExpenseWithID(expenseID: String)
    suspend fun addInstances(code: String): Instance?
}
