package pro.stuermer.dailyexpenses.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase

fun getRoomDatabase(
    builder: RoomDatabase.Builder<ExpensesDatabase>
): ExpensesDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
