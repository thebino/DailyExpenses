package pro.stuermer.dailyexpenses.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sharing(
    @SerialName("id") val id: Int,
    val code: String,
)
