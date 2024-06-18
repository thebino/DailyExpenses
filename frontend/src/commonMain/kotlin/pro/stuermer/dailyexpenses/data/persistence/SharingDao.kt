package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SharingDao {
    @Query("SELECT * FROM sharings")
    fun getSharings(): Flow<List<SharingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sharingCode: SharingEntity)

    @Query("DELETE FROM sharings")
    fun deleteAll()
}
