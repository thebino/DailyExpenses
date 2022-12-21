package pro.stuermer.dailyexpenses.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.domain.model.fakeDomainExpenses
import pro.stuermer.dailyexpenses.domain.model.Expense as DomainExpense
import pro.stuermer.dailyexpenses.data.model.Expense as DataExpense

class ExpensesRepositoryTests {
    private val api: ExpensesApi = mockk()
    private val dao: ExpensesDao = mockk()

    private val repository by lazy {
        ExpensesRepositoryImpl(
            api = api, dao = dao
        )
    }

    @Test
    fun `get expenses should return data for given month`() = runTest {
        // given
        coEvery {
            dao.getExpensesForDate(any(), any())
        } answers {
            flowOf(fakeDomainExpenses.map { it.toPersistenceModel() })
        }

        // when
        val date: LocalDate = LocalDate.parse("2022-02-01")
        val result = repository.getExpensesForDate(date = date).last()

        // then
        assertTrue(actual = result is Resource.Success)
        assertEquals(expected = listOf(fakeDomainExpenses[1]), actual = result.data!!)
        // TODO: get sharing code
        coVerify(exactly = 0) { api.getExpenses("AABBCC") }
    }

    @Test
    fun `get all expenses should not request data from backend`() = runTest {
        // given
        coEvery { dao.getAllExpenses() } answers { flowOf(listOf()) }

        // when
        repository.getExpenses().last()

        // then
        // TODO: get sharing code
        coVerify(exactly = 0) { api.getExpenses("AABBCC") }
    }

    @Test
    fun `get all expenses should request data from database`() = runTest {
        // given
        coEvery { dao.getAllExpenses() } answers { flowOf(listOf()) }

        // when
        repository.getExpenses().last()

        // then
        coVerify(exactly = 1) { dao.getAllExpenses() }
    }

    @Test
    fun `insert should add entry into database`() = runTest {
        // given
        coEvery { dao.insert(any()) } answers {}
        val expense = fakeDomainExpenses[0]

        // when
        repository.addExpense(expense)

        // then
        coVerify(exactly = 1) {
            dao.insert(any())
        }
    }
}
