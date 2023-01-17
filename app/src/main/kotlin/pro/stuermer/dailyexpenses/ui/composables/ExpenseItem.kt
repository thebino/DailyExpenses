package pro.stuermer.dailyexpenses.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.domain.model.Expense
import pro.stuermer.dailyexpenses.domain.model.color
import pro.stuermer.dailyexpenses.domain.model.fakeDomainExpenses
import pro.stuermer.dailyexpenses.domain.model.icon

@Composable
fun ExpenseItem(
    expense: Expense,
    onEditClicked: (expense: Expense) -> Unit,
    onDeleteClicked: (expense: Expense) -> Unit,
) {
    Row(
        modifier = Modifier
            .combinedClickable(
                onClickLabel = stringResource(id = R.string.item_click_action_label_edit),
                onClick = {
                    onEditClicked(expense)
                },
                onLongClickLabel = stringResource(id = R.string.item_click_action_label_delete),
                onLongClick = {
                    onDeleteClicked(expense)
                }
            )
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    shape = RoundedCornerShape(10.dp)
                ),
            enabled = false,
            onClick = {
                // ignore
            }) {
            Icon(
                imageVector = expense.category.icon,
                tint = expense.category.color,
                contentDescription = expense.category.toString()
            )
        }

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = expense.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = expense.category.color
                )
                Text(
                    text = expense.amount.toString().toCurrency(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            val dateString =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(expense.expenseDate)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(10.dp),
                text = dateString,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun ExpenseItemPreview() {
    MaterialTheme {
        ExpenseItem(
            expense = fakeDomainExpenses[0],
            onEditClicked = {},
            onDeleteClicked = {}
        )
    }
}
