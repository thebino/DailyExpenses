package pro.stuermer.balloon.dailyexpenses.data.persistence.model

import org.jetbrains.exposed.sql.Table

data class Expense(
    val id: String,
    val instance: String,
    val category: String,
    val expenseDate: String,
    val creationDate: String,
    val updatedDate: String? = null,
    val deletedDate: String? = null,
    val description: String,
    val amount: Float
)

object ExpensesTable : Table() {
    val id = varchar("id", 255)
    val instance = varchar("instance", 255)
    val category = varchar("category", 128)
    val year = integer("year")
    val month = integer("month")
    val day = integer("day")
    val creationDate = varchar("creationDate", 255)
    val updatedDate = varchar("updatedDate", 255).nullable()
    val deletedDate = varchar("deletedDate", 255).nullable()
    val description = varchar("description", 255)
    val amount = float("amount")

    override val primaryKey = PrimaryKey(id)
}
