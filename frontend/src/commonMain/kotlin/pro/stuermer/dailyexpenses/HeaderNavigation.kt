package pro.stuermer.dailyexpenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HeaderNavigation(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit = {},
) {
    Row(modifier = modifier.statusBarsPadding()) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.clickable(onClick = onNavigateToSettings).padding(8.dp),
            imageVector = Icons.Sharp.Settings,
            tint = Color(0xFF62C386),
            contentDescription = null,
        )
    }
}
