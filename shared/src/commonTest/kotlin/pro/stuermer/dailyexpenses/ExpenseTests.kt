package pro.stuermer.dailyexpenses

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseTests {
    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testSerialization() {
        // given
        val expense = Expense(
            id = "id",
            category = "category",
            expenseDate = "expenseDate",
            creationDate = "creationDate",
            updatedDate = "updatedDate",
            deletedDate = "deletedDate",
            description = "description",
            amount = 0.41f
        )

        // when
        val json = Json.encodeToString(expense)

        // then
        val expected =
            """{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}"""
        assertEquals(expected, json)
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testDeserialization() {
        // given
        val json =
            """{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}"""

        // when
        val expense: Expense = Json.decodeFromString<Expense>(json)

        // then
        val expected = Expense(
            id = "id",
            category = "category",
            expenseDate = "expenseDate",
            creationDate = "creationDate",
            updatedDate = "updatedDate",
            deletedDate = "deletedDate",
            description = "description",
            amount = 0.41f
        )
        assertEquals(expected, expense)
    }
}
