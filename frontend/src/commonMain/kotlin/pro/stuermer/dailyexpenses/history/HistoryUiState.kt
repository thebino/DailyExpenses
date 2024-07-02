package pro.stuermer.dailyexpenses.history

import java.time.LocalDate
import pro.stuermer.dailyexpenses.domain.Expense

data class HistoryUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val items: List<Expense> = listOf(),
    val isLoading: Boolean = false,
    val showInputDialog: Boolean = false,
    val selectedExpense: Expense? = null,
    val error: String? = null
)
