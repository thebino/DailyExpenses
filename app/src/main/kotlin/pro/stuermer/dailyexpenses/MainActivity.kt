package pro.stuermer.dailyexpenses

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.stuermer.dailyexpenses.ui.home.HomeScreen
import pro.stuermer.dailyexpenses.ui.settings.SettingsScreen
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DailyExpensesTheme {
                NavigationHost(
                    orientation = LocalConfiguration.current.orientation
                )
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window?.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    orientation: Int
) {
    NavHost(
        navController = navController,
        startDestination = AppRouting.home
    ) {
        composable(route = AppRouting.home) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(AppRouting.settings)
                }
            )
        }
        composable(route = AppRouting.settings) {
            SettingsScreen(
                onNavigateUp = {
                    navController.popBackStack()
                }
            )
        }
    }
}
