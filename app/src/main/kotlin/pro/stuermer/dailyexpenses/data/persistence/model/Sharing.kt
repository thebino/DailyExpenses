package pro.stuermer.dailyexpenses.data.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sharings")
data class Sharing(
    @PrimaryKey(autoGenerate = true) val identifier: Int? = null,

    val code: String
)
