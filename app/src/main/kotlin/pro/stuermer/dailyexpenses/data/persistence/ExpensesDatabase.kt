package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pro.stuermer.dailyexpenses.data.persistence.model.Expense

@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class, CategoryConverter::class)
abstract class ExpensesDatabase : RoomDatabase() {
    @TypeConverters(DateConverter::class, CategoryConverter::class)
    abstract fun expensesDao(): ExpensesDao
}
