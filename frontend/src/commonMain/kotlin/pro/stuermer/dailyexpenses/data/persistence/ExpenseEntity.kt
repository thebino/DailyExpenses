package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val identifier: String,

    @TypeConverters(CategoryConverter::class)
    val category: String,

    @TypeConverters(DateConverter::class)
    val expenseDate: String,

    @TypeConverters(DateTimeConverter::class)
    val creationDate: String,

    @TypeConverters(DateTimeConverter::class)
    val updatedDate: String?,

    @TypeConverters(DateTimeConverter::class)
    val deletedDate: String?,

    val description: String,

    val amount: Float,
)
