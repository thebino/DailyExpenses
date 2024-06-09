package pro.stuermer.dailyexpenses.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepositoryImpl
import pro.stuermer.dailyexpenses.domain.model.fakeDomainExpenses

class AddExpenseUseCaseTests {
    private val repository: ExpensesRepository = mockk<ExpensesRepositoryImpl>()

    private val useCase by lazy {
        AddExpenseUseCase(
            repository = repository
        )
    }

    @Test
    fun `use case should add a new expense to the repository`() = runTest {
        // given
        coEvery { repository.addExpense(any()) } answers {}
        val expense = fakeDomainExpenses[0]

        // when
        useCase(expense)

        // then
        coVerify(exactly = 1) { repository.addExpense(expense = expense) }
    }
}
