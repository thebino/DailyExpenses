package pro.stuermer.dailyexpenses.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Expense(
    val id: String,
    val category: String,
    val expenseDate: LocalDate,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime? = null,
    val deletedDate: LocalDateTime? = null,
    val description: String,
    val amount: Float,
)
