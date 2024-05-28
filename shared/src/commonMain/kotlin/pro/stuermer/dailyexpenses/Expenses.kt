package pro.stuermer.dailyexpenses

import kotlinx.serialization.Serializable

@Serializable
data class Expenses(
    val items: List<Expense>
)
