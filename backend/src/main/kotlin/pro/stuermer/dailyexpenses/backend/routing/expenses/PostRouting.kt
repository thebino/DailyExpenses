package pro.stuermer.dailyexpenses.backend.routing.expenses

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlin.reflect.typeOf
import pro.stuermer.dailyexpenses.shared.Expense
import pro.stuermer.dailyexpenses.backend.repository.DailyExpensesRepository

fun Route.postIndexRouting(repository: DailyExpensesRepository) {
    // post expenses
    post {
        val instance = call.principal<UserIdPrincipal>()
        call.application.environment.log.info("instance=$instance")
        println("instance=$instance")
        if (instance == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val expenses = call.receive<List<Expense>>()
        println("expenses=$expenses")
        call.application.environment.log.info("+ received ${expenses.size} expenses for ${instance.name}")
        repository.addExpenses(instance.name, expenses)

        call.respond(HttpStatusCode.Accepted)
    } describe {
        tags += "dailyexpense"
        summary = "Create new expenses."
        body {
            description = ""
            required = true
            json {
                schema(
                    typeOf<List<Expense>>(),
                    listOf(
                        Expense(
                            id = "1",
                            category = "1",
                            expenseDate = "2021-01-10",
                            creationDate = "2021-01-10T01:02:03.456+01:00",
                            updatedDate = "2021-01-10T01:02:03.456+01:00",
                            deletedDate = "2021-01-10T01:02:03.456+01:00",
                            description = "Groceries",
                            amount = 1.23f,
                        )
                    )
                )
            }
        }
        202 response {
            description = "Expenses have been accepted."
        }
        401 response {
            description = "Not authorized!"
        }
        security("expenses-basic")
    }
}
