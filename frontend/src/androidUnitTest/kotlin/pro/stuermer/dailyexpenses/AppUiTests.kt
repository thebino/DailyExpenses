package pro.stuermer.dailyexpenses

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import kotlin.test.Test
import pro.stuermer.dailyexpenses.theme.DailyExpensesTheme

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
