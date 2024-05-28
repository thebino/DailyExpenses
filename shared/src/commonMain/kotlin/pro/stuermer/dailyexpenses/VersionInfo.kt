package pro.stuermer.dailyexpenses

import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    val version: String
)
