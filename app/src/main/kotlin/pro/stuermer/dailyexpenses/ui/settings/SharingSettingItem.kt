package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun SharingSettingItem(
    modifier: Modifier = Modifier,
    title: String,
    onSettingClicked: () -> Unit
) {
    SettingItem {
        Column(
            modifier = modifier
                .clickable(
                    onClickLabel = stringResource(id = R.string.settings_share_label_accessibility)
                ) { onSettingClicked() }
                .padding(16.dp)
        ) {
            Row(
                modifier = modifier.padding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
            Text(
                modifier = modifier.padding(top = 4.dp),
                text = "Everyone with the sharing code might access the expenses synchronised with this app.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun SharingSettingItemPreview() {
    DailyExpensesTheme {
        SharingSettingItem(
            title = "Share expenses",
            onSettingClicked = {}
        )
    }
}
