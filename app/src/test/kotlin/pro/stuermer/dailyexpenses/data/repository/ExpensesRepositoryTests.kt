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
import pro.stuermer.dailyexpenses.data.model.SyncStatus
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing
import pro.stuermer.dailyexpenses.domain.model.fakeDomainExpenses

class ExpensesRepositoryTests {
    private val api: ExpensesApi = mockk()
    private val dao: ExpensesDao = mockk()
    private val sharingDao: SharingDao = mockk()

    private val repository by lazy {
        ExpensesRepositoryImpl(
            api = api,
            dao = dao,
            sharingDao = sharingDao
        )
    }

    @Test
    fun `get expenses should return data for given month`() = runTest {
        // given
        val date: LocalDate = LocalDate.parse("2022-02-01")
        coEvery {
            dao.getExpensesForDate(any(), any())
        } answers {
            flowOf(fakeDomainExpenses.filter {
                val fromDate = LocalDate.of(date.year, date.month, 1)
                val toDate = LocalDate.of(date.year, date.month, 1)
                    .plusDays(LocalDate.of(date.year, date.month, 1).lengthOfMonth() - 1L)

                it.expenseDate >= fromDate && it.expenseDate <= toDate
            }.map { it.toPersistenceModel() })
        }
        coEvery { sharingDao.getSharings() } answers { flowOf(listOf()) }

        // when
        val result = repository.getExpensesForDate(date = date).last()

        // then
        assertTrue(actual = result is Resource.Success)
        assertEquals(expected = listOf(fakeDomainExpenses[1]), actual = result.data)
        assertEquals(expected = 1, actual = result.data.size)
        coVerify(exactly = 0) { api.getExpenses("AABBCC") }
    }

    @Test
    fun `get all expenses should not request data from backend`() = runTest {
        // given
        coEvery { dao.getAllExpenses() } answers { flowOf(listOf()) }
        coEvery { sharingDao.getSharings() } answers { flowOf(listOf()) }

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

    @Test
    fun `sync should skip when no sharing group is applied`() = runTest {
        // given
        coEvery { sharingDao.getSharings() } answers { flowOf() }
        coEvery { dao.getAllExpenses() } answers {
            flowOf(fakeDomainExpenses.map { it.toPersistenceModel() })
        }

        coEvery {
            api.addExpenses(any(), any())
        } answers {
            Result.failure(RuntimeException("Test failure"))
        }

        // when
        val result = repository.sync()

        // then
        assertTrue(actual = result is SyncStatus.SyncSkipped)
    }

    @Test
    fun `sync should fail when upload local expenses fails`() = runTest {
        // given
        coEvery { sharingDao.getSharings() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { dao.getAllExpenses() } answers {
            flowOf(fakeDomainExpenses.map { it.toPersistenceModel() })
        }
        coEvery {
            api.addExpenses(any(), any())
        } answers {
            Result.failure(RuntimeException("Mock exception for testing!"))
        }

        // when
        val result = repository.sync()

        // then
        assertTrue(actual = result is SyncStatus.SyncFailed)
        assertEquals(expected = "upload local expenses failed!", actual = result.message)
    }

    @Test
    fun `sync should fail when delete local expenses fails`() = runTest {
        // given
        coEvery { sharingDao.getSharings() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { dao.getAllExpenses() } answers {
            flowOf(fakeDomainExpenses.map { it.toPersistenceModel() })
        }
        coEvery { dao.delete(any()) } answers {}
        coEvery { dao.insert(any()) } answers {}
        coEvery { api.addExpenses(any(), any()) } answers {
            Result.success(true)
        }
        coEvery { api.deleteIds(any(), any()) } answers {
            Result.failure(RuntimeException("Mock exception for testing!"))
        }
        coEvery { api.getExpenses(any()) } answers {
            Result.success(fakeDomainExpenses.map {
                it.toPersistenceModel().toNetworkExpense()
            })
        }

        // when
        val result = repository.sync()

        // then
        assertTrue(actual = result is SyncStatus.SyncFailed)
        assertEquals(expected = "delete local expenses failed!", actual = result.message)
    }

    @Test
    fun `sync should fail when download remote expenses fails`() = runTest {
        // given
        coEvery { sharingDao.getSharings() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { dao.getAllExpenses() } answers {
            flowOf(fakeDomainExpenses.map { it.toPersistenceModel() })
        }
        coEvery { dao.delete(any()) } answers {}
        coEvery { dao.insert(any()) } answers {}
        coEvery { api.addExpenses(any(), any()) } answers {
            Result.success(true)
        }
        coEvery { api.deleteIds(any(), any()) } answers {
            Result.success("foo")
        }
        coEvery { api.getExpenses(any()) } answers {
            Result.failure(RuntimeException("Mock exception for testing!"))
        }

        // when
        val result = repository.sync()

        // then
        assertTrue(actual = result is SyncStatus.SyncFailed)
        assertEquals(expected = "download remote expenses failed", actual = result.message)
    }
}
