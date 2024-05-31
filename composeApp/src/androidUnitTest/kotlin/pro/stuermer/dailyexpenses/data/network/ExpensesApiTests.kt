package pro.stuermer.dailyexpenses.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class ExpensesApiTests {
    @Test
    fun `successful sharing group creation should return sharing code as location header`() = runTest {
        // given

        val api = ExpensesApi.Default(
            httpClient = createMockClient(
                statusCode = HttpStatusCode.Created,
                headers = headersOf(
                    HttpHeaders.ContentType to listOf("application/json"),
                    HttpHeaders.Location to listOf("/api/expense/sharing?code=ABC123")
                ),
                content = ByteReadChannel(
                    """{}""".trimIndent()
                )
            )
        )

        // when
        val result = api.createSharing()

        // then
        assertTrue(actual = result.isSuccess)
        assertEquals(expected = "ABC123", actual = result.getOrNull())
    }

    private fun createMockClient(
        statusCode: HttpStatusCode = HttpStatusCode.OK,
        content: ByteReadChannel,
        headers: Headers = headersOf(HttpHeaders.ContentType, listOf("application/json"))
    ): HttpClient {
        return HttpClient(MockEngine {
            respond(
                content = content,
                status = statusCode,
                headers = headers
            )
        }) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }, contentType = ContentType.Application.Json
                )
            }
        }
    }
}
