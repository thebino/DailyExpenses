package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class AppVersionItemTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displayed_app_version_should_match() {
        // given
        val version = "1.2.3"

        // when
        composeTestRule.setContent {
            AppVersionItem(appVersion = version)
        }

        // then
        composeTestRule.onNodeWithText(version).assertIsDisplayed()
    }
}
