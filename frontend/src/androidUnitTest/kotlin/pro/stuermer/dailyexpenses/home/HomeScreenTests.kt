package pro.stuermer.dailyexpenses.home

import HomeScreen
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import kotlin.test.Test
import pro.stuermer.dailyexpenses.theme.DailyExpensesTheme

class HomeScreenTests {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_3,
        showSystemUi = true,
    )

    @Test
    fun default() {
        paparazzi.snapshot {
            DailyExpensesTheme {
                HomeScreen(
                    totalAmount = 1760.58f,
                    lastTransactions = emptyList()
                )
            }
        }
    }
}
