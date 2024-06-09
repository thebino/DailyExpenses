package pro.stuermer.dailyexpenses.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.domain.model.Expense
import pro.stuermer.dailyexpenses.domain.usecase.AddExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.DeleteExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetExpensesForDateUseCase
import pro.stuermer.dailyexpenses.domain.usecase.StartSyncUseCase
import pro.stuermer.dailyexpenses.domain.usecase.UpdateExpenseUseCase

class HomeViewModel(
    private val getExpensesForDateUseCase: GetExpensesForDateUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val startSyncUseCase: StartSyncUseCase
) : ViewModel() {
    val uiState = MutableStateFlow(HomeScreenState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadExpenses(uiState.value.selectedDate)
        }
    }

    fun handleEvent(homeScreenEvent: HomeScreenEvent) {
        when (homeScreenEvent) {
            HomeScreenEvent.RefreshEvent -> {
                uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    startSyncUseCase()
                    delay(500)
                    uiState.update { it.copy(isLoading = false) }
                }
            }

            HomeScreenEvent.SelectPreviousMonth -> {
                uiState.update { it.copy(selectedDate = it.selectedDate.minusMonths(1)) }
                viewModelScope.launch(Dispatchers.IO) {
                    loadExpenses(uiState.value.selectedDate)
                }
            }

            HomeScreenEvent.SelectNextMonth -> {
                uiState.update { it.copy(selectedDate = it.selectedDate.plusMonths(1)) }
                viewModelScope.launch(Dispatchers.IO) {
                    loadExpenses(uiState.value.selectedDate)
                }
            }

            is HomeScreenEvent.AddEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addExpense(homeScreenEvent.expense)
                    uiState.update { it.copy(showInputDialog = false, selectedExpense = null) }
                }
            }

            is HomeScreenEvent.UpdateEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateExpense(homeScreenEvent.expense)
                    uiState.update { it.copy(showInputDialog = false, selectedExpense = null) }
                }
            }

            HomeScreenEvent.NewItemEvent -> {
                uiState.update { it.copy(showInputDialog = true, selectedExpense = null) }
            }

            HomeScreenEvent.HideInput -> {
                uiState.update { it.copy(showInputDialog = false) }
            }

            is HomeScreenEvent.EditExpenseEvent -> {
                uiState.update {
                    it.copy(
                        showInputDialog = true, selectedExpense = homeScreenEvent.expense
                    )
                }
            }

            is HomeScreenEvent.DeleteExpenseEvenr -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteExpense(homeScreenEvent.expense)
                }
            }
        }
    }

    private suspend fun deleteExpense(expense: Expense) {
        deleteExpenseUseCase(expense)
    }

    private suspend fun addExpense(expense: Expense) {
        addExpenseUseCase(expense)
    }

    private suspend fun updateExpense(expense: Expense) {
        updateExpenseUseCase(expense)
    }

    internal suspend fun loadExpenses(date: LocalDate) {
        getExpensesForDateUseCase(date).collect { result ->
            when (result) {
                is Resource.Error -> {
                    uiState.update {
                        it.copy(
                            error = result.message?.message, isLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    uiState.update {
                        it.copy(
                            isLoading = true, error = null
                        )
                    }
                }

                is Resource.Success -> {
                    uiState.update {
                        it.copy(
                            isLoading = false, error = null, items = result.data
                        )
                    }
                }
            }
        }
    }
}
