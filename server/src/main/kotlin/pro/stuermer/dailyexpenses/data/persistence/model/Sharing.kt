package pro.stuermer.dailyexpenses.data.persistence.model

import kotlinx.serialization.Serializable

@Serializable
data class Sharing(
    val id: Int,
    val code: String
)
