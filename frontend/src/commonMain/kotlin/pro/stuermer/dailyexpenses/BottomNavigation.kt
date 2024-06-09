package pro.stuermer.dailyexpenses

import androidx.compose.foundation.layout.*
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
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = Modifier.padding(bottom = 10.dp).padding(start = 250.dp).size(50.dp),
        imageVector = Icons.Outlined.AddCircle,
        tint = Color(0xFFE0ED67),
        contentDescription = null
    )
    Row(
        modifier = modifier.navigationBarsPadding().fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.Filled.Home,
            tint = Color(0xFF62C386),
            contentDescription = null
        )
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = vectorResource(Res.drawable.query_stats_24px),
            tint = Color(0xFF4C514E),
            contentDescription = null
        )
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.AutoMirrored.Filled.List,
            tint = Color(0xFF4C514E),
            contentDescription = null
        )
    }
}
