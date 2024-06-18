package pro.stuermer.dailyexpenses.di

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase
import pro.stuermer.dailyexpenses.data.persistence.SharingDatabase

fun getExpensesDatabaseBuilder(): RoomDatabase.Builder<ExpensesDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "expenses.db")
    return Room.databaseBuilder<ExpensesDatabase>(
        name = dbFile.absolutePath,
    )
}

fun getSharingDatabaseBuilder(): RoomDatabase.Builder<SharingDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "sharings.db")
    return Room.databaseBuilder<SharingDatabase>(
        name = dbFile.absolutePath,
    )
}
