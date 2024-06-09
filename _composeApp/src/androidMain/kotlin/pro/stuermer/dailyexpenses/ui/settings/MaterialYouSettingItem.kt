package pro.stuermer.dailyexpenses.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

@Composable
fun MaterialYouSettingItem(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    enabled: Boolean = false,
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    SettingItem {
        val materialYouEnabledState = if (checked) {
            stringResource(id = R.string.settings_material_enabled_accessibility)
        } else {
            stringResource(id = R.string.settings_material_disabled_accessibility)
        }
        SettingItem(modifier = modifier) {
            Row(modifier = Modifier
                .toggleable(
                    value = checked, onValueChange = onCheckedChanged, role = Role.Switch
                )
                .semantics {
                    stateDescription = materialYouEnabledState
                }
                .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.35f),
                    text = title
                )
                Switch(
                    enabled = enabled, checked = checked, onCheckedChange = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun MaterialYouSettingItemPreview() {
    DailyExpensesTheme {
        MaterialYouSettingItem(
            title = "Use wallpaper colors",
            checked = true,
            onCheckedChanged = {})
    }
}
