package pro.stuermer.dailyexpenses.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pro.stuermer.dailyexpenses.data.splitDouble
import pro.stuermer.dailyexpenses.theme.DailyExpensesTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel { HomeViewModel() },
    navController: NavHostController = rememberNavController()
) {
    HomeScreen(
        uiState = viewModel.uiState.collectAsState().value,
        handleEvent = viewModel::handleEvent,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    handleEvent: (event: HomeScreenEvent) -> Unit,
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TotalAmount(
            modifier = Modifier.padding(horizontal = 10.dp), uiState = uiState
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Total Amount",
            style = DailyExpensesTheme.typography.legendLabel
        )

        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
            text = "Last transactions",
            style = DailyExpensesTheme.typography.title,
        )

        LastTransactions(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), uiState = uiState
        )
    }
}

@Composable
private fun TotalAmount(
    modifier: Modifier = Modifier, uiState: HomeUiState
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        if (uiState.leadingCurrency) {
            Text(
                modifier = Modifier,
                text = "$",
                style = DailyExpensesTheme.typography.totalAmountIntegerPart,
            )
        }

        val (integerPart, fractionalPart) = splitDouble(uiState.totalAmount)

        Text(
            modifier = Modifier,
            text = "$integerPart",
            style = DailyExpensesTheme.typography.totalAmountIntegerPart,
        )
        Text(
            modifier = Modifier,
            text = ".$fractionalPart",
            style = DailyExpensesTheme.typography.totalAmountFractionalPart,

            )

        if (!uiState.leadingCurrency) {
            Text(
                modifier = Modifier,
                text = "€",
                style = DailyExpensesTheme.typography.totalAmountIntegerPart,
            )
        }
    }
}

@Composable
private fun LastTransactions(
    modifier: Modifier = Modifier, uiState: HomeUiState
) {
    if (uiState.lastTransactions.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            items(items = uiState.lastTransactions) { item ->
                Text(
                    text = "item: ${item.description}",
                    color = DailyExpensesTheme.colors.accentPrimary,
                    style = DailyExpensesTheme.typography.totalAmountFractionalPart
                )
            }
        }
    } else {
        // TODO: add image and proper text
        Text(
            modifier = modifier,
            text = "No entries",
            style = DailyExpensesTheme.typography.legendLabel
        )
    }
}
