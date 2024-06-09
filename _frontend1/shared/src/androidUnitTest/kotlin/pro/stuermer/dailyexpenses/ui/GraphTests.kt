package pro.stuermer.dailyexpenses.ui

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import kotlinx.coroutines.delay
import org.junit.Rule
import pro.stuermer.dailyexpenses.ui.model.Category
import pro.stuermer.dailyexpenses.ui.model.GraphData
import pro.stuermer.dailyexpenses.ui.model.color
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme
import kotlin.test.Test

class GraphTests {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_3,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    @Test
    fun default() {
        paparazzi.gif(
            view = ComposeView(paparazzi.context).apply {
                setContent {
                    DailyExpensesTheme {
                        LaunchedEffect(Unit) {
                            delay(150)
                        }
                        Graph(
                            chartData = listOf(
                                GraphData(
                                    category = Category.Hobby,
                                    value = 6247,
                                    color = Category.Hobby.color,
                                    label = "Cocktails with friends"
                                ),
                                GraphData(
                                    category = Category.Grocery,
                                    value = 2819,
                                    color = Category.Grocery.color,
                                    label = "Uber eats"
                                ),
                                GraphData(
                                    category = Category.Other,
                                    value = 492,
                                    color = Category.Other.color,
                                    label = "Gift card"
                                ),
                                GraphData(
                                    category = Category.Living,
                                    value = 17287,
                                    color = Category.Living.color,
                                    label = "Gas & Electric"
                                ),
                                GraphData(
                                    category = Category.Grocery,
                                    value = 2575,
                                    color = Category.Grocery.color,
                                    label = "Groceries"
                                ),
                            ), halfcircle = true
                        )
                    }
                }
            }, start = 300L, end = 1200L, fps = 8
        )
    }
}
