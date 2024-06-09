package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SectionSpacer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.12f
                )
            )
    )
}

@Preview
@Composable
fun SectionSpacerPreview() {
    MaterialTheme {
        SectionSpacer(modifier = Modifier.fillMaxWidth())
    }
}
