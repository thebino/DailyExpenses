package pro.stuermer.dailyexpenses.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import pro.stuermer.dailyexpenses.data.persistence.model.Expense as PersistenceExpense

data class Expense(
    val identifier: String = UUID.randomUUID().toString(),
    val amount: Float,
    val description: String,
    val category: Category = Category.Grocery,
    val expenseDate: LocalDate = LocalDate.now(),
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val updatedDate: LocalDateTime? = null,
    val deletedDate: LocalDateTime? = null,
) {
    fun toPersistenceModel(): PersistenceExpense = PersistenceExpense(
        identifier = identifier,
        category = category,
        expenseDate = expenseDate,
        creationDate = creationDate,
        updatedDate = updatedDate,
        deletedDate = deletedDate,
        description = description,
        amount = amount
    )

    override fun toString(): String {
        return "DomainExpense(expenseDate=$expenseDate, amount=$amount)"
    }
}

val fakeDomainExpenses = listOf<Expense>(
    Expense(
        identifier = "cec5f4e3-1251-4898-9376-8fddf324354f",
        amount = 12f,
        description = "Grocery",
        category = Category.Grocery,
        expenseDate = LocalDate.parse("2022-01-01")
    ),
    Expense(
        identifier = "745dc283-2c58-4347-9ea0-35f2031daa9f",
        amount = 4.12f,
        description = "Grocery",
        category = Category.Grocery,
        expenseDate = LocalDate.parse("2022-02-02")
    ),
    Expense(
        identifier = "1ea0537c-9f50-4f85-b3ca-4dd2513e3cd7",
        amount = 56.50f,
        description = "High Roller",
        category = Category.Restaurant,
        expenseDate = LocalDate.parse("2022-03-03")
    ),
)
