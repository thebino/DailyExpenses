package pro.stuermer.dailyexpenses.ui.composables

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

/**
 * appends a currency before/after the input text
 */
class CurrencyVisualTransformation(
    private val currency: String = "€",
    private val placement: CurrencyPlacement = CurrencyPlacement.TRAILING,
) : VisualTransformation {

    private val symbols = DecimalFormat().decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val decimalSeparator = symbols.decimalSeparator
        val thousandsSeparator = symbols.groupingSeparator

        // remove decimal separator
        val input = text.text.replace(decimalSeparator.toString(), "")

        // split input
        val exponent = input.dropLast(2).reversed().chunked(3).joinToString(thousandsSeparator.toString()).reversed()
        val fraction = input.takeLast(2)

        // add decimal separator for visual
        val formattedNumber = "$exponent$decimalSeparator$fraction"

        val currencyString: AnnotatedString = when (placement) {
            CurrencyPlacement.TRAILING -> AnnotatedString("$formattedNumber $currency")
            CurrencyPlacement.LEADING -> AnnotatedString(" $currency $formattedNumber")
        }

        return TransformedText(
            text = currencyString,
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = currencyString.length - 2
                override fun transformedToOriginal(offset: Int): Int = text.length
            }
        )
    }
}

fun String.toCurrency(
    currency: String = "€",
    placement: CurrencyPlacement = CurrencyPlacement.TRAILING,
): String {
    return when (placement) {
        CurrencyPlacement.TRAILING -> "$this $currency"
        CurrencyPlacement.LEADING -> " $currency $this"
    }
}

enum class CurrencyPlacement {
    LEADING,
    TRAILING
}

fun String.toCurrencyFloat(): Float {
    val input = this.filter { char: Char ->
        char.isDigit()
    }
    val exponent = input.dropLast(2)
    val fraction = input.takeLast(2)
    val formattedNumber = "$exponent.$fraction"

    return formattedNumber.toFloat()
}
