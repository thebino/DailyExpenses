package pro.stuermer.dailyexpenses.ui.home

import java.time.LocalDate
import pro.stuermer.dailyexpenses.domain.model.Expense

data class HomeScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val items: List<Expense> = listOf(),
    val isLoading: Boolean = false,
    val showInputDialog: Boolean = false,
    val selectedExpense: Expense? = null,
    val error: String? = null
)
