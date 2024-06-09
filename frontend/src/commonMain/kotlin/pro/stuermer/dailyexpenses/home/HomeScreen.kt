import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(modifier: Modifier) {
    Column {
        Text(
            modifier = modifier,
            text = "This is the new material free Ui",
            color = Color(0xFF62C386)
        )
        Text(
            modifier = modifier,
            text = "$1760,58",
            color = Color(0xFFE0ED67)
        )
    }
}
