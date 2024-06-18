package pro.stuermer.dailyexpenses.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase
import pro.stuermer.dailyexpenses.data.persistence.SharingDatabase

fun getExpensesDatabaseBuilder(ctx: Context): RoomDatabase.Builder<ExpensesDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("expenses.db")
    return Room.databaseBuilder<ExpensesDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getSharingDatabaseBuilder(ctx: Context): RoomDatabase.Builder<SharingDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("sharings.db")
    return Room.databaseBuilder<SharingDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
