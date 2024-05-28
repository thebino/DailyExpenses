package pro.stuermer.balloon.dailyexpenses.routing

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import kotlin.reflect.typeOf
import pro.stuermer.balloon.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.deleteIndexRouting(repository: DailyExpensesRepository) {
    // delete expenses
    delete {
        val instance = call.principal<UserIdPrincipal>()
        if (instance == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@delete
        }

        val expenseIDs: List<String> = call.receive()
        call.application.environment.log.info("delete IDs: [${expenseIDs.joinToString()}]")

        expenseIDs.forEach { expenseId: String ->
            repository.deleteExpenseWithID(expenseID = expenseId)
        }

        call.respond(HttpStatusCode.OK)
    } describe {
        tags += "dailyexpense"
        summary = "Delete the expense with the given ID."
        body {
            required = true
            json {
                schema(
                    typeOf<List<String>>(),
                    listOf<String>(
                        "8e5ce19e-72c5-4cd9-aae8-1005b5defd1b",
                        "08154359-b6ef-458b-a7cc-51a9fe8bcc15",
                        "b680f580-265d-468a-aa3e-103449d69b73"
                    )
                )
            }
        }
    }
}
