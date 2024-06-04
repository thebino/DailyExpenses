package pro.stuermer.dailyexpenses.shared

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SharingTests {
    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testSerialization() {
        // given
        val sharing = Sharing(
            id = 1,
            code = "ABC123"
        )

        // when
        val json = Json.encodeToString(sharing)

        // then
        val expected = """{"id":1,"code":"ABC123"}"""
        assertEquals(expected, json)
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testDeserialization() {
        // given
        val json =  """{"id":1,"code":"ABC123"}""".trimIndent()

        // when
        val sharing: Sharing = Json.decodeFromString<Sharing>(json)

        // then
        val expected = Sharing(
            id = 1,
            code = "ABC123"
        )
        assertEquals(expected, sharing)
    }
}
