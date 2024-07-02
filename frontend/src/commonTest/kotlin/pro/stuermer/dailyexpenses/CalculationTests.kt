package pro.stuermer.dailyexpenses

import kotlin.test.assertEquals
import org.junit.Test
import pro.stuermer.dailyexpenses.data.splitDouble

class CalculationTests {
    @Test
    fun float_split_should_succeed() {
        // given
        val amount: Double = 1760.58

        // when
        val (integerPart, fractionalPart) = splitDouble(amount)

        // then
        assertEquals(1760, integerPart)
        assertEquals(58, fractionalPart)
    }
}
