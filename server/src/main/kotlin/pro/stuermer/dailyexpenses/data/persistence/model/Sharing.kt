package pro.stuermer.balloon.dailyexpenses.data.persistence.model

import kotlinx.serialization.Serializable

@Serializable
data class Sharing(
    val id: Int,
    val code: String
)
