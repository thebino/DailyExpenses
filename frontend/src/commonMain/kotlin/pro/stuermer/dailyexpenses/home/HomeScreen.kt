import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pro.stuermer.dailyexpenses.domain.Expense
import pro.stuermer.dailyexpenses.home.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel { HomeViewModel() },
    navController: NavHostController = rememberNavController()
) {
    val uiState = viewModel.uiState.collectAsState()

    HomeScreen(totalAmount = 1760.58f, lastTransactions = emptyList())
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    totalAmount: Float,
    lastTransactions: List<Expense>
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = "This is the new material free Ui",
            color = Color(0xFF62C386)
        )
        Row {

            Text(
                modifier = Modifier,
                text = "$1760",
                color = Color(0xFFE0ED67)
            )
            Text(
                modifier = Modifier,
                text = ".58",
                color = Color(0xFFE0ED67)
            )
        }
    }
}
