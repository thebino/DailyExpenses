package pro.stuermer.balloon.dailyexpenses.data.persistence.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Instance(
    val id: Int,
    val code: String
)

object Instances : Table() {
    val id = integer("id").autoIncrement()
    val code = varchar("code", 255)

    override val primaryKey = PrimaryKey(Instances.id)
}
