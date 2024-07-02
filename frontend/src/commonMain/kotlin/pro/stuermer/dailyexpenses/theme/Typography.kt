package pro.stuermer.dailyexpenses.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dailyexpenses.frontend.generated.resources.Res
import dailyexpenses.frontend.generated.resources.neuropolitical
import org.jetbrains.compose.resources.Font

/**
 * staticComposition implementation without a font family or other implementations
 */
val LocalTypography = staticCompositionLocalOf {
    ExpensesTypography(
        title = TextStyle(),
        totalAmountIntegerPart = TextStyle(),
        totalAmountFractionalPart = TextStyle(),
        legendLabel = TextStyle(),
        legendValue = TextStyle(),
        historyCategory = TextStyle(),
        historyDescription = TextStyle(),
        historyValue = TextStyle(),
    )
}

@Immutable
data class ExpensesTypography(
    val title: TextStyle,

    // home
    val totalAmountIntegerPart: TextStyle,
    val totalAmountFractionalPart: TextStyle,

    // graph
    val legendLabel: TextStyle,
    val legendValue: TextStyle,

    // history
    val historyCategory: TextStyle,
    val historyDescription: TextStyle,
    val historyValue: TextStyle,
)
