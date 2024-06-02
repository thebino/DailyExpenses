package pro.stuermer.dailyexpenses.shared

import kotlinx.serialization.Serializable

@Serializable
data class Expenses(
    val items: List<Expense>
)
