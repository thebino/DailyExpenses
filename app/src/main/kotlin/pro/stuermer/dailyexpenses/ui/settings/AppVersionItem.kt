package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun AppVersionItem(
    modifier: Modifier = Modifier,
    appVersion: String
) {
    SettingItem(modifier = modifier) {
        val semanticsContent = stringResource(R.string.settings_app_version_content_accessibility, appVersion)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .semantics(mergeDescendants = true) {
                    stateDescription = semanticsContent
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.settings_app_version_label)
            )
            Text(text = appVersion)
        }
    }
}

@Preview
@Composable
fun AppVersionItemPreview() {
    DailyExpensesTheme {
        AppVersionItem(
            modifier = Modifier.fillMaxWidth(),
            appVersion = "1.0.0"
        )
    }
}
