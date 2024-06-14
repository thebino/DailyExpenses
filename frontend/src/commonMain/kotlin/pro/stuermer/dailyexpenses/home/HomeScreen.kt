import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pro.stuermer.dailyexpenses.home.HomeViewModel
//import org.koin.android.ext.android.inject

//import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel { HomeViewModel() },
    navController: NavHostController = rememberNavController()
) {
    viewModel.uiState.collectAsState()
    Column {
        Text(
            modifier = modifier,
            text = "This is the new material free Ui",
            color = Color(0xFF62C386)
        )
        Text(
            modifier = modifier,
            text = "$1760,58",
            color = Color(0xFFE0ED67)
        )
    }
}
