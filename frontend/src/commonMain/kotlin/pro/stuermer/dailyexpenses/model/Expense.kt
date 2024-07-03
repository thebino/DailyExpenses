package pro.stuermer.dailyexpenses.model

import kotlinx.datetime.*
import pro.stuermer.dailyexpenses.randomUUID

data class Expense(
    val identifier: String = randomUUID(),
    val amount: Float,
    val description: String,
    val category: Category = Category.Grocery,
    val expenseDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    val creationDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val updatedDate: LocalDateTime? = null,
    val deletedDate: LocalDateTime? = null,
) {
    override fun toString(): String {
        return "DomainExpense(expenseDate=$expenseDate, amount=$amount)"
    }
}
