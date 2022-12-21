package pro.stuermer.dailyexpenses.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    onDeleteClicked = onDeleteClicked
                )
            }
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
