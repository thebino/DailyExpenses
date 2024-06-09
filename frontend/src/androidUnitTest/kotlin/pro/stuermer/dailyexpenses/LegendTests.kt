package pro.stuermer.dailyexpenses.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme
import kotlin.test.Test

class LegendTests {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_3,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    @Test
    fun default() {
        paparazzi.snapshot {
            DailyExpensesTheme {
                Legend(chartData = fakeChartData)
            }
        }
    }
}
