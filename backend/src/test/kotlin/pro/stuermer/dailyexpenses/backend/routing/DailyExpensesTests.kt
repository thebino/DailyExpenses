package pro.stuermer.dailyexpenses.backend.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import pro.stuermer.dailyexpenses.shared.VersionInfo

class DailyExpensesTests {
    @Test
    fun `get root should succeed`() = testApplication {
        // given
        val versionInfo = VersionInfo(version = "1.4.0")
        val expected = Json.encodeToString(versionInfo)

        // when
        val response = client.get("/")

        // then
        kotlin.test.assertEquals(HttpStatusCode.OK, response.status)
        kotlin.test.assertEquals(expected, response.bodyAsText())
    }
}
