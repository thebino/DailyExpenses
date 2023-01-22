package pro.stuermer.dailyexpenses.data.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import pro.stuermer.dailyexpenses.data.persistence.CategoryConverter
import pro.stuermer.dailyexpenses.data.persistence.DateConverter
import pro.stuermer.dailyexpenses.data.persistence.DateTimeConverter
import pro.stuermer.dailyexpenses.domain.model.Category
import pro.stuermer.dailyexpenses.domain.model.Expense as DomainExpense
import pro.stuermer.dailyexpenses.data.model.Expense as NetworkExpense

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey val identifier: String = UUID.randomUUID().toString(),

    @TypeConverters(CategoryConverter::class)
    val category: Category,

    @TypeConverters(DateConverter::class)
    val expenseDate: LocalDate,

    @TypeConverters(DateTimeConverter::class)
    val creationDate: LocalDateTime,

    @TypeConverters(DateTimeConverter::class)
    val updatedDate: LocalDateTime?,

    @TypeConverters(DateTimeConverter::class)
    val deletedDate: LocalDateTime?,

    val description: String,

    val amount: Float,
) {
    fun toDomainModel(): DomainExpense {
        return DomainExpense(
            identifier = identifier,
            category = category,
            expenseDate = expenseDate,
            creationDate = creationDate,
            updatedDate = updatedDate,
            deletedDate = deletedDate,
            description = description,
            amount = amount,
        )
    }

    fun toNetworkExpense(): NetworkExpense = NetworkExpense(
        id = identifier,
        category = category,
        expenseDate = expenseDate,
        creationDate = creationDate,
        updatedDate = updatedDate,
        deletedDate = deletedDate,
        description = description,
        amount = amount
    )
}

val fakeDataExpense1 = Expense(
    category = Category.Grocery,
    amount = 12f,
    description = "01/01",
    creationDate = LocalDateTime.parse("2022-01-01T01:02:03.456+01:00"),
    expenseDate = LocalDate.parse("2022-01-01"),
    updatedDate = LocalDateTime.parse("2022-01-01T01:02:03.456+01:00"),
    deletedDate = null
)

val fakeDataExpense2 = fakeDataExpense1.copy(
    description = "02/02",
    creationDate = LocalDateTime.parse("2022-02-02T01:02:03.456+01:00"),
    expenseDate = LocalDate.parse("2022-02-02"),
    updatedDate = LocalDateTime.parse("2022-02-02T01:02:03.456+01:00")
)

val fakeDataExpense3 = fakeDataExpense1.copy(
    description = "03/03",
    creationDate = LocalDateTime.parse("2022-03-03T01:02:03.456+01:00"),
    expenseDate = LocalDate.parse("2022-03-03"),
    updatedDate = LocalDateTime.parse("2022-03-03T01:02:03.456+01:00")
)
