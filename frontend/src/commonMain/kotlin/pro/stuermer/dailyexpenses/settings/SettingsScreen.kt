package pro.stuermer.dailyexpenses.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Settings",
        color = Color(0xFF62C386)
    )
}
