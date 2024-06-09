package pro.stuermer.dailyexpenses.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.navigationBarsPadding().fillMaxWidth()) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.Filled.Home,
            tint = Color.White,
            contentDescription = null,
        )
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.Filled.Home,
            tint = Color.White,
            contentDescription = null,
        )
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.Filled.Home,
            tint = Color.White,
            contentDescription = null,
        )
    }
}
