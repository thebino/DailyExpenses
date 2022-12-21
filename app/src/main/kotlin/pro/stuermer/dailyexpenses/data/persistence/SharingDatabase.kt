package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing

@Database(
    entities = [Sharing::class],
    version = 1,
    exportSchema = true
)
abstract class SharingDatabase : RoomDatabase() {
    abstract fun sharingsDao(): SharingDao
}
