package pro.stuermer.dailyexpenses

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import pro.stuermer.dailyexpenses.ui.home.HomeScreen
import pro.stuermer.dailyexpenses.ui.settings.SettingsScreen
import pro.stuermer.dailyexpenses.ui.theme.DailyExpensesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            DailyExpensesTheme {
                NavHost(
                    navController = navController,
                    startDestination = "expenses"
                ) {
                    composable(
                        route = "expenses",
                        arguments = listOf(
                            navArgument("description") {
                                nullable = true
                                defaultValue = null
                            }
                        ),
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "dailyexpenses://add/{description}"
                            }
                        )
                    ) { backStackEntry ->
                    HomeScreen(
                            onNavigateToSettings = {
                                navController.navigate(AppRouting.settings)
                            },
                            description = backStackEntry.arguments?.getString("description")
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
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window?.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}
