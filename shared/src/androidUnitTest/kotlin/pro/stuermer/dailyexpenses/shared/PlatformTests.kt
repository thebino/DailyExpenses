package pro.stuermer.dailyexpenses.shared

import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformTests {
    @Test
    fun platform_for_Android_should_return_Android_version() {
        // given
        val expected = "Android 0"

        // when
        val platform: Platform = AndroidPlatform()

        // then
        assertEquals(expected = expected, actual = platform.name)
    }

    @Test
    fun platform_for_Android_should_return_the_right_platform() {
        // given
        val expected = "Android 0"

        // when
        val platform = getPlatform()

        // then
        assertEquals(expected = expected, actual = platform.name)
    }
}
