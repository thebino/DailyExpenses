package pro.stuermer.dailyexpenses.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalSemanticColors = staticCompositionLocalOf {
    ExpensesSemanticColors(
        backgroundPrimary = Color(0xFF101416),
        backgroundSurface = Color(0xFF1C2022),
        foregroundPrimary = Color(0xFF006D3F),
        foregroundSecondary = Color(0xFF808B00),
        disabledPrimary = Color(0xFF),
        disabledSecondary = Color(0xFF),
        accentPrimary = Color(0xFF),
        accentSecondary = Color(0xFF),
        criticalPrimary = Color(0xFF),
        criticalSecondary = Color(0xFF),
        successPrimary = Color(0xFF),
        successSecondary = Color(0xFF),
        cautionPrimary = Color(0xFF),
        cautionSecondary = Color(0xFF),
    )
}

@Immutable
data class ExpensesSemanticColors(
    // Background
    val backgroundPrimary: Color,
    val backgroundSurface: Color,

    // Foreground
    val foregroundPrimary: Color,
    val foregroundSecondary: Color,

    // disabled
    val disabledPrimary: Color,
    val disabledSecondary: Color,

    // Primary
    val accentPrimary: Color,
    val accentSecondary: Color,

    // Critical
    val criticalPrimary: Color,
    val criticalSecondary: Color,

    // Success
    val successPrimary: Color,
    val successSecondary: Color,

    // Caution
    val cautionPrimary: Color,
    val cautionSecondary: Color,
)
