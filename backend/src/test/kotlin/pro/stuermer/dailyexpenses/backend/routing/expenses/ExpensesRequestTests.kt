package pro.stuermer.dailyexpenses.backend.routing.expenses

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.plugin
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import pro.stuermer.dailyexpenses.backend.AUTH_NAME_EXPENSES
import pro.stuermer.dailyexpenses.backend.expensesModule
import pro.stuermer.dailyexpenses.backend.mainModule
import pro.stuermer.dailyexpenses.backend.persistence.Instances
import pro.stuermer.dailyexpenses.backend.persistence.ExpensesTable
import pro.stuermer.dailyexpenses.shared.Expense
import java.util.Base64
import java.util.UUID

class ExpensesRequestTests {

    @Test
    fun `get expenses without authentication should fail`() = testApplication {
        // given
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api")

        // then
        Assert.assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `get expenses with authentication should succeed`() = testApplication {
        // given
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            mainModule()
            expensesModule(testing = true)

            transaction {
                Instances.insert {
                    it[code] = "A1B2C3"
                }
            }
        }
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api") {
            val credentials = Base64.getEncoder().encodeToString("A1B2C3:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
        }

        // then
        Assert.assertEquals(HttpStatusCode.OK, response.status)
        Assert.assertEquals(0, response.body<List<Expense>>().size)
    }

    @Test
    fun `get expenses with auth should return all expenses`() = testApplication {
        // given
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            mainModule()
            // install fake authentication for testing
            plugin(Authentication).configure {
                basic(AUTH_NAME_EXPENSES) {
                    validate {
                        UserIdPrincipal("test")
                    }
                }
            }
            expensesModule(testing = true, installAuth = false)

            transaction {
                ExpensesTable.insert {
                    it[id] = "1"
                    it[instance] = "test"
                    it[category] = "1"
                    it[year] = 2021
                    it[month] = 1
                    it[day] = 10
                    it[creationDate] = "2021-01-10"
                    it[description] = "description 1"
                    it[amount] = 1.23f
                }
                ExpensesTable.insert {
                    it[id] = "2"
                    it[instance] = "test"
                    it[category] = "2"
                    it[year] = 2022
                    it[month] = 2
                    it[day] = 20
                    it[creationDate] = "2022-02-20"
                    it[description] = "description 2"
                    it[amount] = 3.14f
                }
            }
        }
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api") {
            val credentials = Base64.getEncoder().encodeToString("test:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
        }

        // then
        Assert.assertEquals(HttpStatusCode.OK, response.status)
        Assert.assertEquals(2, response.body<List<Expense>>().size)
    }

    @Test
    fun `get expenses with auth and date query should return only filtered expenses`() =
        testApplication {
            // given
            environment {
                config = ApplicationConfig("application-test.conf")
            }
            application {
                mainModule()
                // install fake authentication for testing
                plugin(Authentication).configure {
                    basic(AUTH_NAME_EXPENSES) {
                        validate {
                            UserIdPrincipal("test")
                        }
                    }
                }
                expensesModule(testing = true, installAuth = false)

                transaction {
                    ExpensesTable.deleteAll()
                    ExpensesTable.insert {
                        it[id] = "1"
                        it[instance] = "test"
                        it[category] = "1"
                        it[year] = 2021
                        it[month] = 1
                        it[day] = 10
                        it[creationDate] = "2021-01-10"
                        it[description] = "description 1"
                        it[amount] = 1.23f
                    }
                    ExpensesTable.insert {
                        it[id] = "2"
                        it[instance] = "test"
                        it[category] = "2"
                        it[year] = 2022
                        it[month] = 2
                        it[day] = 20
                        it[creationDate] = "2022-02-20"
                        it[description] = "description 2"
                        it[amount] = 3.14f
                    }
                }
            }
            val httpClient = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            // when
            val response = httpClient.get("/api?year=2022&month=2") {
                val credentials = Base64.getEncoder().encodeToString("test:".toByteArray())
                headers.append(HttpHeaders.Authorization, "Basic $credentials")
            }

            // then
            Assert.assertEquals(HttpStatusCode.OK, response.status)
            Assert.assertEquals(1, response.body<List<Expense>>().size)
            Assert.assertEquals("2022-02-20", response.body<List<Expense>>()[0].expenseDate)
        }

    @Ignore // Flaky test
    @Test
    fun `post new expenses should add items to database`() = testApplication {
        // given
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            mainModule()
            // install fake authentication for testing
            plugin(Authentication).configure {
                basic(AUTH_NAME_EXPENSES) {
                    validate {
                        UserIdPrincipal("test")
                    }
                }
            }
            expensesModule(testing = true, installAuth = false)

            transaction {
                ExpensesTable.deleteAll()
            }
        }
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val expense1 = Expense(
            id = "6D7B2C4D-3FA3-40A6-88CC-9561DA26C55B",
            category = "category",
            expenseDate = "2022-11-27",
            creationDate = "2022-11-27T09:15:30",
            updatedDate = "2022-11-27T10:15:30",
            deletedDate = null,
            description = "description",
            amount = 1.0f,
        )
        val expense2 = Expense(
            id = "D1F5E3E5-4246-49E6-8629-5D0B531CF10D",
            category = "category",
            expenseDate = "2022-11-27",
            creationDate = "2022-11-27T11:15:30",
            updatedDate = "2022-11-27T12:15:30",
            deletedDate = null,
            description = "description",
            amount = 1.0f,
        )
        val response = httpClient.post("/api") {
            val credentials = Base64.getEncoder().encodeToString("test:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
            contentType(ContentType.Application.Json)
            setBody(listOf(expense1, expense2))
        }

        // then
        Assert.assertEquals(HttpStatusCode.Accepted, response.status)

        val row = transaction {
            ExpensesTable.selectAll().map(::resultRowToExpense)
        }

        Assert.assertNotNull(row)
        Assert.assertEquals(2, row.size)
        // check 1st expense
//        Assert.assertEquals("6D7B2C4D-3FA3-40A6-88CC-9561DA26C55B", row[0].id)
//        Assert.assertEquals("2022-11-27T09:15:30", row[0].creationDate)
//        Assert.assertEquals("2022-11-27T10:15:30", row[0].updatedDate)
//
//        // check 2nd expense
//        Assert.assertEquals("2022-11-27T11:15:30", row[1].creationDate)
//        Assert.assertEquals("D1F5E3E5-4246-49E6-8629-5D0B531CF10D", row[1].id)
//        Assert.assertEquals("2022-11-27T12:15:30", row[1].updatedDate)
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun `put changes into existing expense should succeed`() = testApplication {
        // given
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            mainModule()
            // install fake authentication for testing
            plugin(Authentication).configure {
                basic(AUTH_NAME_EXPENSES) {
                    validate {
                        UserIdPrincipal("test")
                    }
                }
            }
            expensesModule(testing = true, installAuth = false)

            transaction {
                ExpensesTable.deleteAll()
                ExpensesTable.insert {
                    it[id] = "ad95512e-1703-431b-9492-3339dc7a7f01"
                    it[instance] = "test"
                    it[category] = "1"
                    it[year] = 2021
                    it[month] = 1
                    it[day] = 10
                    it[creationDate] = "2011-12-03T10:15:30"
                    it[description] = "description 1"
                    it[amount] = 1.23f
                }
            }
        }
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.put("/api") {
            val credentials = Base64.getEncoder().encodeToString("test:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
            headers.append(HttpHeaders.ContentType, "application/json")
            setBody(
                Expense(
                    id = "ad95512e-1703-431b-9492-3339dc7a7f01",
                    category = "category",
                    expenseDate = "2021-01-11",
                    creationDate = "2011-12-03T10:15:30",
                    updatedDate = "2011-12-03T10:15:30",
                    // deletedDate is only set in DELETE call
                    description = "description 2",
                    amount = 2.34f,
                )
            )
        }

        // then
        Assert.assertEquals(HttpStatusCode.OK, response.status)
        val row = transaction {
            ExpensesTable.select { ExpensesTable.instance eq "test" and (ExpensesTable.id eq "ad95512e-1703-431b-9492-3339dc7a7f01") }
                .limit(1).firstOrNull()
        }

        Assert.assertNotNull(row)
        Assert.assertEquals("ad95512e-1703-431b-9492-3339dc7a7f01", row!![ExpensesTable.id])
        Assert.assertEquals("test", row[ExpensesTable.instance])
        Assert.assertEquals("category", row[ExpensesTable.category])
        Assert.assertEquals(2021, row[ExpensesTable.year])
        Assert.assertEquals(1, row[ExpensesTable.month])
        Assert.assertEquals(11, row[ExpensesTable.day])
        Assert.assertEquals("2011-12-03T10:15:30", row[ExpensesTable.creationDate])
        Assert.assertEquals("2011-12-03T10:15:30", row[ExpensesTable.updatedDate])
        Assert.assertEquals("description 2", row[ExpensesTable.description])
        Assert.assertEquals(2.34f, row[ExpensesTable.amount])
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun `delete expense should succeed`() = testApplication {
        // given
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            mainModule()
            // install fake authentication for testing
            plugin(Authentication).configure {
                basic(AUTH_NAME_EXPENSES) {
                    validate {
                        UserIdPrincipal("test")
                    }
                }
            }
            expensesModule(testing = true, installAuth = false)

            transaction {
                ExpensesTable.deleteAll()
                ExpensesTable.insert {
                    it[id] = "ad95512e-1703-431b-9492-3339dc7a7f0f"
                    it[instance] = "test"
                    it[category] = "1"
                    it[year] = 2021
                    it[month] = 1
                    it[day] = 10
                    it[creationDate] = "2021-01-10"
                    it[description] = "description 1"
                    it[amount] = 1.23f
                }
                ExpensesTable.insert {
                    it[id] = "74ef4dfc-4932-4275-aa72-d3e453ad74d3"
                    it[instance] = "test"
                    it[category] = "1"
                    it[year] = 2021
                    it[month] = 1
                    it[day] = 10
                    it[creationDate] = "2021-01-10"
                    it[description] = "description 1"
                    it[amount] = 1.23f
                }
            }
        }
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.delete("/api") {
            val credentials = Base64.getEncoder().encodeToString("test:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
            headers.append(HttpHeaders.ContentType, "application/json")
            setBody(
                listOf<String>(
                    "ad95512e-1703-431b-9492-3339dc7a7f0f",
//                    "74ef4dfc-4932-4275-aa72-d3e453ad74d3"
                )
            )
        }

        // then
        Assert.assertEquals(HttpStatusCode.OK, response.status)
        val expenses = transaction {
            ExpensesTable.select { ExpensesTable.id eq "ad95512e-1703-431b-9492-3339dc7a7f0f" or (ExpensesTable.id eq "74ef4dfc-4932-4275-aa72-d3e453ad74d3") }
                .map(::resultRowToExpense)
        }

        Assert.assertNotNull(expenses)
        Assert.assertEquals("74ef4dfc-4932-4275-aa72-d3e453ad74d3", expenses[0].id)
        Assert.assertNull(expenses[0].deletedDate)
        Assert.assertEquals("ad95512e-1703-431b-9492-3339dc7a7f0f", expenses[1].id)
        Assert.assertNotNull(expenses[1].deletedDate)
    }

    @Suppress("MaximumLineLength", "MaxLineLength")
    private fun resultRowToExpense(row: ResultRow) =
        pro.stuermer.dailyexpenses.backend.persistence.Expense(
            id = row[ExpensesTable.id],
            instance = row[ExpensesTable.instance],
            category = row[ExpensesTable.category],
            expenseDate = "${row[ExpensesTable.year]}-${
                row[ExpensesTable.month].toString().padStart(2, '0')
            }-${row[ExpensesTable.day].toString().padStart(2, '0')}",
            creationDate = row[ExpensesTable.creationDate],
            updatedDate = row[ExpensesTable.updatedDate],
            deletedDate = row[ExpensesTable.deletedDate],
            description = row[ExpensesTable.description],
            amount = row[ExpensesTable.amount],
        )
}