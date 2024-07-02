@file:Suppress("MatchingDeclarationName")

package pro.stuermer.dailyexpenses.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dailyexpenses.frontend.generated.resources.Res
import dailyexpenses.frontend.generated.resources.neuropolitical
import org.jetbrains.compose.resources.Font

object DailyExpensesTheme {
    val colors: ExpensesSemanticColors
        @Composable @ReadOnlyComposable get() = LocalSemanticColors.current

    val typography: ExpensesTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    val dimensions: ExpensesDimensions
        @Composable @ReadOnlyComposable get() = LocalDimensions.current
}

@Composable
fun DailyExpensesTheme(
    colors: ExpensesSemanticColors = DailyExpensesTheme.colors,
    dimensions: ExpensesDimensions = DailyExpensesTheme.dimensions,
    content: @Composable () -> Unit
) {
    val neuropoliticalFamily = FontFamily(
        Font(
            resource = Res.font.neuropolitical,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        ), Font(
            resource = Res.font.neuropolitical,
            weight = FontWeight.Light,
            style = FontStyle.Normal,
        )
    )

    // overwrite staticComposition with a font familiy included implementation
    val typography = ExpensesTypography(
        title = TextStyle(
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            fontFamily = neuropoliticalFamily,
            lineHeight = 80.sp,
        ),
        totalAmountIntegerPart = TextStyle(
            color = Color(0xFFE0ED67),
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraLight,
            fontFamily = neuropoliticalFamily,
        ),
        totalAmountFractionalPart = TextStyle(
            color = Color(0xFFE0ED67),
            fontSize = 25.sp,
            fontWeight = FontWeight.Light,
            fontFamily = neuropoliticalFamily,
        ),

        legendLabel = TextStyle(
            color = Color(0xFF6F7272)
        ),
        legendValue = TextStyle(
            color = Color.Magenta
        ),

        historyCategory = TextStyle(
            color = Color.Magenta
        ),
        historyDescription = TextStyle(
            color = Color.Magenta
        ),
        historyValue = TextStyle(
            color = Color.Magenta
        ),
    )

    CompositionLocalProvider(
        LocalSemanticColors provides colors,
        LocalTypography provides typography,
        LocalDimensions provides dimensions,
    ) {
        MaterialTheme(
            colorScheme = placeholderColorScheme,
        ) {
            content()
        }
    }
}

// mark all material colors - use own colors only
private val placeholderColorScheme = ColorScheme(
    primary = Magenta,
    onPrimary = Magenta,
    primaryContainer = Magenta,
    onPrimaryContainer = Magenta,
    secondary = Magenta,
    onSecondary = Magenta,
    secondaryContainer = Magenta,
    onSecondaryContainer = Magenta,
    tertiary = Magenta,
    onTertiary = Magenta,
    tertiaryContainer = Magenta,
    onTertiaryContainer = Magenta,
    error = Magenta,
    errorContainer = Magenta,
    onError = Magenta,
    onErrorContainer = Magenta,
    background = Magenta,
    onBackground = Magenta,
    surface = Magenta,
    onSurface = Magenta,
    surfaceVariant = Magenta,
    onSurfaceVariant = Magenta,
    outline = Magenta,
    inverseOnSurface = Magenta,
    inverseSurface = Magenta,
    inversePrimary = Magenta,
    surfaceTint = Magenta,
    outlineVariant = Magenta,
    scrim = Magenta,
)
