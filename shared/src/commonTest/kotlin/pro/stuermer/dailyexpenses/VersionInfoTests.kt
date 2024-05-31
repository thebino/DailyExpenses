package pro.stuermer.dailyexpenses

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class VersionInfoTests {
    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testSerialization() {
        // given
        val versionInfo = VersionInfo(version = "1.2.3")

        // when
        val json = Json.encodeToString(versionInfo)

        // then
        val expected =
            """{"version":"1.2.3"}""".trimMargin()
        assertEquals(expected, json)
    }
}
