package pro.stuermer.dailyexpenses.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "History",
        color = Color(0xFF62C386)
    )
}
