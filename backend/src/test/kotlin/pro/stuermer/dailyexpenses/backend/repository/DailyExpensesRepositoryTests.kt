package pro.stuermer.dailyexpenses.backend.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import pro.stuermer.dailyexpenses.shared.Expense
import kotlin.test.Test
import kotlin.test.assertEquals

class DailyExpensesRepositoryTests {
    @Test
    fun `shoud return empty list if database is cleared`() = runBlocking {
        // given
        val repository = DailyExpensesRepositoryImpl(testing = false)
        repository.deleteAll()
        delay(2000)
        val expected = listOf<Expense>()

        // when
        val expenses = repository.getExpenses("ABC123")

        // then
        assertEquals(expected, expenses)
    }

    @Test
    fun `shoud return items if database is not empty`() = runBlocking {
        // given
        val repository = DailyExpensesRepositoryImpl(testing = true)
        repository.deleteAll()
        val expense = Expense(
            id = "id",
            category = "category",
            expenseDate = "2021-10-11",
            creationDate = "creationDate",
            updatedDate = "updatedDate",
            deletedDate = null,
            description = "description",
            amount = 0.41f
        )
        repository.addExpenses("ABC123", listOf(expense))
        val expected = listOf<Expense>(expense)

        // when
        val expenses = repository.getExpenses("ABC123")

        // then
        assertEquals(1, expenses.size)
        assertEquals(expected, expenses)
    }
}
