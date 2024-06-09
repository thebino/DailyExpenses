package pro.stuermer.dailyexpenses.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import pro.stuermer.dailyexpenses.Res
import pro.stuermer.dailyexpenses.neuropolitical

val LocalTypography = staticCompositionLocalOf {
    ExpensesTypography(
        titleBold = TextStyle(
//            fontFamily = neuropoliticalFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 64.sp,
            lineHeight = 72.sp
        ),
        labelSmall = TextStyle(
//            fontFamily = neuropoliticalFamily,
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
//            lineHeight = 72.sp
        ),
    )
}

@Immutable
data class ExpensesTypography(
    val titleBold: TextStyle,
    val labelSmall: TextStyle,
)

@Composable
fun Typography(): Typography {
    val neuropoliticalFamily = FontFamily(
        Font(
            resource = Res.font.neuropolitical,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        ),
        Font(
            resource = Res.font.neuropolitical,
            weight = FontWeight.Light,
            style = FontStyle.Normal,
        )
    )

    return Typography(
//        bodyLarge = TextStyle(
//            fontFamily = FontFamily.Default,
//            fontWeight = FontWeight.Normal,
//            fontSize = 16.sp,
//            lineHeight = 24.sp,
//            letterSpacing = 0.5.sp
//        ),
//        titleLarge = TextStyle(
//            fontFamily = neuropoliticalFamily,
//            fontWeight = FontWeight.Light,
//            fontSize = 22.sp,
//            lineHeight = 28.sp,
//            letterSpacing = 0.sp
//        ),
//        titleMedium = TextStyle(
//            fontFamily = neuropoliticalFamily,
//            fontWeight = FontWeight.Medium,
//            fontSize = 14.sp,
//            lineHeight = 16.sp,
//            letterSpacing = 0.7.sp
//        ),
//        titleSmall = TextStyle(
//            fontFamily = neuropoliticalFamily,
//            fontWeight = FontWeight.Medium,
//            fontSize = 12.sp,
//            lineHeight = 14.sp,
//            letterSpacing = 0.5.sp
//        ),
    )
}
