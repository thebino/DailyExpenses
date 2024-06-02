package pro.stuermer.dailyexpenses.backend.routing.sharings

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Test
import pro.stuermer.dailyexpenses.backend.expensesModule
import pro.stuermer.dailyexpenses.backend.mainModule
import pro.stuermer.dailyexpenses.backend.persistence.Instances

class SharingsRequestTest {
    @Test
    fun `post a new sharing group should respond with a valid code`() = testApplication {
        // when
        val response = client.post("/api/sharing")

        // then
        Assert.assertEquals(HttpStatusCode.Created, response.status)
        Assert.assertNotNull(response.headers[HttpHeaders.Location])
        Assert.assertTrue(response.headers[HttpHeaders.Location]!!.contains("/api/sharing"))
    }

    @Test
    fun `get a sharing group with valid code should succeed`() = testApplication {
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

        // given
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api/sharing?code=A1B2C3")

        // then
        Assert.assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `get a sharing group with a too short code should fail`() = testApplication {
        // given
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api/sharing?code=A1B2")

        // then
        Assert.assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `get a sharing group with an invalid code should fail`() = testApplication {
        // given
        val httpClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = httpClient.get("/api/sharing?code=A1B2?x")

        // then
        Assert.assertEquals(HttpStatusCode.NotAcceptable, response.status)
    }

}