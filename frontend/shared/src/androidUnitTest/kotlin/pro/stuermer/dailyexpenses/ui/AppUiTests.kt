package pro.stuermer.dailyexpenses.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme
import kotlin.test.Test

class AppUiTests {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_3,
        showSystemUi = true,
    )

    @Test
    fun default() {
        paparazzi.snapshot {
            DailyExpensesTheme {
                AppUi()
            }
        }
    }
}
