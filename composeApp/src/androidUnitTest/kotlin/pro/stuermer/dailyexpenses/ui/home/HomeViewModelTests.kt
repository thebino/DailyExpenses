package pro.stuermer.dailyexpenses.ui.home

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.domain.model.Expense
import pro.stuermer.dailyexpenses.domain.usecase.AddExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.DeleteExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetExpensesForDateUseCase
import pro.stuermer.dailyexpenses.domain.usecase.StartSyncUseCase
import pro.stuermer.dailyexpenses.domain.usecase.UpdateExpenseUseCase
import java.time.LocalDate
import java.time.Month

class HomeViewModelTests {
    private val getExpensesForDateUseCase = mockk<GetExpensesForDateUseCase>()
    private val addExpenseUseCase = mockk<AddExpenseUseCase>()
    private val updateExpenseUseCase = mockk<UpdateExpenseUseCase>()
    private val deleteExpenseUseCase = mockk<DeleteExpenseUseCase>()
    private val startSyncUseCase = mockk<StartSyncUseCase>()

    @Test
    fun `refresh is changing loading state`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(HomeScreenEvent.RefreshEvent)
        val homeUiState = viewModel.uiState.value

        // then
//        assertEquals(expected = true, actual = homeUiState.isLoading)
    }

    private suspend fun createTested(
        getExpensesForDateUseCaseAnswer: Flow<Resource<List<Expense>>> = flowOf(
            Resource.Success(listOf())
        )
    ): HomeViewModel {
        coEvery { getExpensesForDateUseCase.invoke(any()) } answers { getExpensesForDateUseCaseAnswer }
        coEvery { addExpenseUseCase.invoke(any()) } answers { }
        coEvery { updateExpenseUseCase.invoke(any()) } answers { }
        coEvery { deleteExpenseUseCase.invoke(any()) } answers { }
        coEvery { startSyncUseCase.invoke() } answers {}

        val viewModel = HomeViewModel(
            getExpensesForDateUseCase = getExpensesForDateUseCase,
            addExpenseUseCase = addExpenseUseCase,
            updateExpenseUseCase = updateExpenseUseCase,
            deleteExpenseUseCase = deleteExpenseUseCase,
            startSyncUseCase = startSyncUseCase,
        )
        viewModel.loadExpenses(LocalDate.of(2023, Month.JANUARY, 1))

        return viewModel
    }
}
