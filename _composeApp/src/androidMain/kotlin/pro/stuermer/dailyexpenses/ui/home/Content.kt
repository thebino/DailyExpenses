package pro.stuermer.dailyexpenses.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.stuermer.dailyexpenses.R
import pro.stuermer.dailyexpenses.domain.model.Expense
import pro.stuermer.dailyexpenses.domain.model.fakeDomainExpenses
import pro.stuermer.dailyexpenses.ui.composables.ExpenseItem

@Composable
fun Content(
    modifier: Modifier = Modifier,
    items: List<Expense> = emptyList(),
    onEditClicked: (expense: Expense) -> Unit,
    onDeleteClicked: (expense: Expense) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    val expenseToDelete = remember { mutableStateOf<Expense?>(null) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(items = items) {
                ExpenseItem(
                    expense = it,
                    onEditClicked = onEditClicked,
                    onDeleteClicked = { expense: Expense ->
                        expenseToDelete.value = expense
                    }
                )
            }
        }

        if (expenseToDelete.value != null) {
            AlertDialog(
                onDismissRequest = { expenseToDelete.value = null },
                title = {
                    Text(text = stringResource(id = R.string.item_click_action_label_delete))
                },
                text = {
                    Text(text = stringResource(id = R.string.item_click_action_description_delete))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            expenseToDelete.value?.let {
                                onDeleteClicked(it)
                            }
                            expenseToDelete.value = null
                        }
                    ) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            expenseToDelete.value = null
                        }
                    ) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    MaterialTheme {
        Content(
            items = fakeDomainExpenses,
            onEditClicked = {},
            onDeleteClicked = {}
        )
    }
}
