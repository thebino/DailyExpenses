package pro.stuermer.dailyexpenses

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExpenseTests {
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
        val expected = """{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}"""
        assertEquals(expected, json)
    }

    @Test
    fun testDeserialization() {
        // given
        val json = """{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}"""

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
