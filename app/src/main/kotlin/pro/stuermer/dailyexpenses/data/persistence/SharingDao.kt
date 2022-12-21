package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing

@Dao
interface SharingDao {
    @Query("SELECT * FROM sharings")
    fun getSharings(): Flow<List<Sharing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sharingCode: Sharing)

    @Query("DELETE FROM sharings")
    fun deleteAll()
}
