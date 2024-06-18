package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SharingEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SharingDatabase : RoomDatabase() {
    abstract fun sharingDao(): SharingDao
}
