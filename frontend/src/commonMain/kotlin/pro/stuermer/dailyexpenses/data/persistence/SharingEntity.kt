package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sharings")
data class SharingEntity(
    @PrimaryKey(autoGenerate = true) val identifier: Int? = null,
    val code: String
)
