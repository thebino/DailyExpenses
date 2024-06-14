package pro.stuermer.dailyexpenses.home

data class HomeUiState(
//    val selectedDate: LocalDate = LocalDate.now(),
//    val items: List<Expense> = listOf(),
    val isLoading: Boolean = false,
    val showInputDialog: Boolean = false,
//    val selectedExpense: Expense? = null,
    val error: String? = null
)
