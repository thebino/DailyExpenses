package pro.stuermer.dailyexpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pro.stuermer.dailyexpenses.history.HistoryScreen
import pro.stuermer.dailyexpenses.home.HomeScreen
import pro.stuermer.dailyexpenses.settings.SettingsScreen
import pro.stuermer.dailyexpenses.statistics.StatisticsScreen
import pro.stuermer.dailyexpenses.theme.DailyExpensesTheme

@Composable
fun AppUi(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    DailyExpensesTheme {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()
        // Get the name of the current screen
        val currentScreen = Destinations.valueOf(
            backStackEntry?.destination?.route ?: Destinations.Home.name
        )

        Column(
            modifier = modifier.fillMaxSize().background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderNavigation(
                onNavigateToSettings = {
                    navController.navigate(Destinations.Settings.name)
                }
            )

            // content
            NavHost(
                navController = navController,
                startDestination = Destinations.Home.name,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(20.dp)
            ) {
                composable(route = Destinations.Home.name) {
                    HomeScreen()
                }

                composable(route = Destinations.Statistics.name) {
                    StatisticsScreen()
                }

                composable(route = Destinations.History.name) {
                    HistoryScreen()
                }

                composable(route = Destinations.Settings.name) {
                    SettingsScreen()
                }
            }

            BottomNavigation(
                currentScreen = currentScreen,
                onNavigateToHome = {
                    navController.navigate(Destinations.Home.name)
                },
                onNavigateToStatistics = {
                    navController.navigate(Destinations.Statistics.name)
                },
                onNavigateToHistory = {
                    navController.navigate(Destinations.History.name)
                },
            )
        }
    }
}
