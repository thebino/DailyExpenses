package pro.stuermer.dailyexpenses.data.network

import android.accounts.NetworkErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.util.Base64
import pro.stuermer.dailyexpenses.data.model.Expense

interface ExpensesApi {

    suspend fun createSharing(): Result<String>
    suspend fun joinSharing(code: String): Result<Boolean>
    suspend fun getExpenses(code: String): Result<List<Expense>>
    suspend fun getExpensesForDate(code: String, year: Int, month: Int): Result<List<Expense>>
    suspend fun addExpenses(code: String, expenses: List<Expense>): Result<Boolean>
    suspend fun deleteIds(code: String, localDeletedIds: List<String>): Result<String>

    @Suppress("TooGenericExceptionCaught")
    class Default(private val httpClient: HttpClient) : ExpensesApi {
        override suspend fun createSharing(): Result<String> {
            return try {
                val result = httpClient.post("/api/expense/sharing")
                if (result.status == HttpStatusCode.Created) {
                    val location = result.headers[HttpHeaders.Location]
                    if (location?.startsWith("/api/expense/sharing?code=") == true) {
                        val code =
                            location.replace("/api/expense/sharing?code=", "")
                        Result.success(code)
                    } else {
                        Result.failure(NoSuchFieldException(""))
                    }
                } else {
                    Result.failure(NoSuchFieldException("Sharing group not created"))
                }
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

        override suspend fun joinSharing(code: String): Result<Boolean> {
            return try {
                val result = httpClient.get("/api/expense/sharing?code=$code")
                Result.success(result.status == HttpStatusCode.OK)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

        override suspend fun getExpenses(code: String): Result<List<Expense>> {
            return try {
                val result = httpClient.get("/api/expense") {
                    val credentials = Base64.getEncoder().encodeToString("$code:".toByteArray())
                    headers[HttpHeaders.Authorization] = "Basic $credentials"
                }.body<List<Expense>>()
                Result.success(result)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

        override suspend fun getExpensesForDate(code: String, year: Int, month: Int): Result<List<Expense>> {
            return try {
                httpClient.get("/api/expense?year=$year&month=$month") {
                    val credentials = Base64.getEncoder().encodeToString("$code:".toByteArray())
                    headers[HttpHeaders.Authorization] = "Basic $credentials"
                }.body()
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

        override suspend fun addExpenses(code: String, expenses: List<Expense>): Result<Boolean> {
            return try {
                val response = httpClient.post("/api/expense") {
                    val credentials = Base64.getEncoder().encodeToString("$code:".toByteArray())
                    headers[HttpHeaders.Authorization] = "Basic $credentials"
                    contentType(ContentType.Application.Json)
                    setBody(expenses)
                }
                if (response.status == HttpStatusCode.Accepted) {
                    Result.success(true)
                } else {
                    Result.failure(NetworkErrorException("Response status was ${response.status}"))
                }
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

        override suspend fun deleteIds(code: String, localDeletedIds: List<String>): Result<String> {
            return try {
                val response = httpClient.delete("/api/expense") {
                    val credentials = Base64.getEncoder().encodeToString("$code:".toByteArray())
                    headers[HttpHeaders.Authorization] = "Basic $credentials"
                    setBody(localDeletedIds)
                }
                Result.success((response.status == HttpStatusCode.OK).toString())
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }
    }
}
