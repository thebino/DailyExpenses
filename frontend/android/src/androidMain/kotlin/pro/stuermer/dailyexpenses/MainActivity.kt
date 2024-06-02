package pro.stuermer.dailyexpenses

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import pro.stuermer.dailyexpenses.ui.AppUi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()

            DisposableEffect(systemUiController, isDarkMode) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode,
                )

                onDispose {}
            }

            AppUi()
        }



//        setContent {
//            val navController = rememberNavController()
//
//            DailyExpensesTheme {
//                NavHost(
//                    navController = navController,
//                    startDestination = AppRouting.HOME
//                ) {
//                    composable(AppRouting.HOME) {
//                        HomeScreen(
//                            onNavigateToSettings = {
//                                navController.navigate(AppRouting.SETTINGS)
//                            }
//                        )
//                    }
//                    composable(
//                        route = "${AppRouting.EXPENSES}?description={description}&amount={amount}",
//                        arguments = listOf(
//                            navArgument("description") {
//                                type = NavType.StringType
//                                nullable = true
//                                defaultValue = null
//                            },
//                            navArgument("amount") {
//                                type = NavType.FloatType
//                                nullable = false
//                                defaultValue = 0.0f
//                            }
//                        ),
//                        deepLinks = listOf(
//                            navDeepLink {
//                                uriPattern = "expenses://add/?description={description}&amount={amount}"
//                            }
//                        )
//                    ) { backStackEntry ->
//                        val description = backStackEntry.arguments?.getString("description")
//                        val amount = backStackEntry.arguments?.getFloat("amount")
//                        HomeScreen(
//                            onNavigateToSettings = {
//                                navController.navigate(AppRouting.SETTINGS)
//                            },
//                            description = description,
//                            amount = amount
//                        )
//                    }
//                    composable(route = AppRouting.SETTINGS) {
//                        SettingsScreen(
//                            onNavigateUp = {
//                                navController.popBackStack()
//                            }
//                        )
//                    }
//                }
//            }
//        }
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            @Suppress("DEPRECATION")
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        } else {
//            window?.insetsController?.apply {
//                hide(WindowInsets.Type.statusBars())
//                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        }
    }
}
