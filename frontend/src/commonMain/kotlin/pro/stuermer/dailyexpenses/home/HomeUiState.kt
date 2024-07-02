package pro.stuermer.dailyexpenses.home

import pro.stuermer.dailyexpenses.domain.Expense

data class HomeUiState(
    val loadingTransactions: Boolean = false,
    val loadingAmount: Boolean = false,
    val totalAmount: Double = 1760.58,
    val leadingCurrency: Boolean = true,
    val lastTransactions: List<Expense> = listOf(),
    val error: String? = null
)
