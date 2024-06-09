package pro.stuermer.dailyexpenses.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sharing(
    @SerialName("id") val id: Int,
    val code: String,
)
