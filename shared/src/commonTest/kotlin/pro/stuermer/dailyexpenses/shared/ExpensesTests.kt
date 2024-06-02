package pro.stuermer.dailyexpenses.shared

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpensesTests {
    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testSerialization() {
        // given
        val expenses = Expenses(
            items = listOf(
                Expense(
                    id = "id",
                    category = "category",
                    expenseDate = "expenseDate",
                    creationDate = "creationDate",
                    updatedDate = "updatedDate",
                    deletedDate = "deletedDate",
                    description = "description",
                    amount = 0.41f
                )
            )
        )

        // when
        val json = Json.encodeToString(expenses)

        // then
        val expected =
            """{"items":[{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}]}"""
        assertEquals(expected, json)
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun testDeserialization() {
        // given
        val json =
            """{"items":[{"id":"id","category":"category","expenseDate":"expenseDate","creationDate":"creationDate","updatedDate":"updatedDate","deletedDate":"deletedDate","description":"description","amount":0.41}]}"""

        // when
        val expenses: Expenses = Json.decodeFromString<Expenses>(json)

        // then
        val expected = Expenses(
            items = listOf(
                Expense(
                    id = "id",
                    category = "category",
                    expenseDate = "expenseDate",
                    creationDate = "creationDate",
                    updatedDate = "updatedDate",
                    deletedDate = "deletedDate",
                    description = "description",
                    amount = 0.41f
                )
            )
        )
        assertEquals(expected, expenses)
    }
}
