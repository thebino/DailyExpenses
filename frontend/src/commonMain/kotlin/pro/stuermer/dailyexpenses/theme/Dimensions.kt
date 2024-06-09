package pro.stuermer.dailyexpenses.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalDimensions = staticCompositionLocalOf {
    ExpensesDimensions()
}

@Immutable
data class ExpensesDimensions(
    // Progress indicator
    val progressIndicatorSize: Dp = 24.dp,
)
