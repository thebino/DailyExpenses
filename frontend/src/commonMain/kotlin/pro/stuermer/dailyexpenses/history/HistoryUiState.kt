package pro.stuermer.dailyexpenses.history

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pro.stuermer.dailyexpenses.domain.Expense

data class HistoryUiState(
    val selectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    val items: List<Expense> = listOf(),
    val isLoading: Boolean = false,
    val showInputDialog: Boolean = false,
    val selectedExpense: Expense? = null,
    val error: String? = null
)
