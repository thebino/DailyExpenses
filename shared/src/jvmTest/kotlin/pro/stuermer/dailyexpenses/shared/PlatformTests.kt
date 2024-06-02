package pro.stuermer.dailyexpenses.shared

import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformTests {
    @Test
    fun `Platform for jvm should return java version`() {
        // given
        val expected = "Java 17.0.1"

        // when
        val platform: Platform = JVMPlatform()

        // then
        assertEquals(expected = expected, actual = platform.name)
    }


    @Test
    fun `Platform for jvm should return the right platform`() {
        // given
        val expected = "Java 17.0.1"

        // when
        val platform = getPlatform()

        // then
        assertEquals(expected = expected, actual = platform.name)
    }
}
