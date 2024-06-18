package pro.stuermer.dailyexpenses.di

import androidx.room.Room
import androidx.room.RoomDatabase
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase
import pro.stuermer.dailyexpenses.data.persistence.SharingDatabase

fun getExpensesDatabaseBuilder(): RoomDatabase.Builder<ExpensesDatabase> {
    val dbFilePath = NSHomeDirectory() + "/expenses.db"
    return Room.databaseBuilder<ExpensesDatabase>(
        name = dbFilePath,
        factory = { ExpensesDatabase::class.instantiateImpl() }
    )
}

fun getSharingDatabaseBuilder(): RoomDatabase.Builder<SharingDatabase> {
    val dbFilePath = NSHomeDirectory() + "/sharings.db"
    return Room.databaseBuilder<SharingDatabase>(
        name = dbFilePath,
        factory = { SharingDatabase::class.instantiateImpl() }
    )
}
