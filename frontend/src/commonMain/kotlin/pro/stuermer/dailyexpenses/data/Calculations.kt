package pro.stuermer.dailyexpenses.data

/**
 * Splitting the integer and fractional parts of a floating point number
 */
fun splitDouble(number: Double): Pair<Int, Int> {
    val parts = number.toString().split(".")
    val integerPart = parts[0].toInt()
    val fractionalPart = if (parts.size > 1) parts[1].toInt() else 0

    return Pair(integerPart, fractionalPart)
}
