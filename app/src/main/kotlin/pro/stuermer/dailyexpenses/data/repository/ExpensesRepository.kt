package pro.stuermer.dailyexpenses.data.repository

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.domain.model.Expense

interface ExpensesRepository {
    suspend fun getExpensesForDate(date: LocalDate): Flow<Resource<List<Expense>>>
    suspend fun getExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun sync(): Boolean
}
