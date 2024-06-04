package pro.stuermer.dailyexpenses.backend.routing.categories

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pro.stuermer.dailyexpenses.backend.expensesModule
import pro.stuermer.dailyexpenses.backend.mainModule
import pro.stuermer.dailyexpenses.backend.persistence.Instances
import pro.stuermer.dailyexpenses.shared.VersionInfo
import java.util.Base64
import kotlin.test.Test

class CategoriesRequestTests {
    @Test
    fun `categories should be returned`() = testApplication {
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

        // when
        val response = client.get("/api/categories") {
            val credentials = Base64.getEncoder().encodeToString("A1B2C3:".toByteArray())
            headers.append(HttpHeaders.Authorization, "Basic $credentials")
        }

        // then
        kotlin.test.assertEquals(HttpStatusCode.NotImplemented, response.status)
    }
}
