package pro.stuermer.dailyexpenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dailyexpenses.frontend.generated.resources.Res
import dailyexpenses.frontend.generated.resources.query_stats_24px
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    currentScreen: Destinations,
    onNavigateToHome: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
) {
    Icon(
        modifier = Modifier.clickable(onClick = onNavigateToHome)
            .padding(bottom = 10.dp)
            .padding(start = 250.dp)
            .size(50.dp),
        imageVector = Icons.Outlined.AddCircle,
        tint = Color(0xFFE0ED67),
        contentDescription = null
    )
    Row(
        modifier = modifier.navigationBarsPadding().fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.clickable(onClick = onNavigateToHome).padding(8.dp),
            imageVector = Icons.Filled.Home,
            tint = isScreenActiveColor(currentScreen, Destinations.Home.name),
            contentDescription = null
        )
        Icon(
            modifier = Modifier.clickable(onClick = onNavigateToStatistics).padding(8.dp),
            imageVector = vectorResource(Res.drawable.query_stats_24px),
            tint = isScreenActiveColor(currentScreen, Destinations.Statistics.name),
            contentDescription = null
        )
        Icon(
            modifier = Modifier.clickable(onClick = onNavigateToHistory).padding(8.dp),
            imageVector = Icons.AutoMirrored.Filled.List,
            tint = isScreenActiveColor(currentScreen, Destinations.History.name),
            contentDescription = null
        )
    }
}

/**
 * Return the color to tint the icon and label based on the current active screen
 */
private fun isScreenActiveColor(currentScreen: Destinations, name: String): Color {
    return if (currentScreen.name == name) {
        Color(0xFF62C386)
    } else {
        Color(0xFF4C514E)
    }
}
