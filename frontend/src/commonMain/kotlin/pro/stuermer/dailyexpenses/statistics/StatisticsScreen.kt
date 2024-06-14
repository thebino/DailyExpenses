package pro.stuermer.dailyexpenses.statistics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Statistics",
        color = Color(0xFF62C386)
    )
}
