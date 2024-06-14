package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Expense::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class, DateTimeConverter::class, CategoryConverter::class)
abstract class ExpensesDatabase : RoomDatabase() {
    @TypeConverters(DateConverter::class, DateTimeConverter::class, CategoryConverter::class)
    abstract fun expensesDao(): ExpensesDao
}
