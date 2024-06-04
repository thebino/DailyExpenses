package pro.stuermer.dailyexpenses.backend.routing.categories

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import pro.stuermer.dailyexpenses.backend.repository.DailyExpensesRepository

fun Route.getCategoriesRouting(repository: DailyExpensesRepository) {
    // get most used category for description text
    get("/categories") {
        val input: String = call.request.queryParameters["input"] ?: ""

        // TODO: get category for input (last 3 month)

        call.respond(HttpStatusCode.NotImplemented)
    } describe {
        tags += "dailyexpense"
        summary = "get most used category for description text."
        security("expenses-basic")
    }
}
