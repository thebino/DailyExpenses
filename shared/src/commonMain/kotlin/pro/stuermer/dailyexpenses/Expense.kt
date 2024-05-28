package pro.stuermer.dailyexpenses

import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: String,
    val category: String,
    val expenseDate: String,
    val creationDate: String,
    val updatedDate: String? = null,
    val deletedDate: String? = null,
    val description: String,
    val amount: Float,
)
