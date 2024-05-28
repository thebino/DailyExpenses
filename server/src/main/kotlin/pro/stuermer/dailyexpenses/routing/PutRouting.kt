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
import io.ktor.server.routing.put
import kotlin.reflect.typeOf
import pro.stuermer.balloon.dailyexpenses.data.repository.DailyExpensesRepository
import pro.stuermer.dailyexpenses.Expense


fun Route.putIndexRouting(repository: DailyExpensesRepository) {
    // update expenses
    put {
        val instance = call.principal<UserIdPrincipal>()
        if (instance == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@put
        }

        val newExpense: Expense = call.receive()
        call.application.environment.log.info("expense: '$newExpense'")

        val persistedExpense = repository.getExpenseForId(instance = instance.name, id = newExpense.id)
        if (persistedExpense == null) {
            call.respond(HttpStatusCode.NotFound, "Expense not found!")
            return@put
        }

        val updatedCount = repository.updateExpenseWithId(instance.name, newExpense)
        if (updatedCount == 1) {
            call.respond(HttpStatusCode.OK)
            return@put
        }
        call.respond(HttpStatusCode.InternalServerError)

    } describe {
        tags += "dailyexpense"
        summary = "Update the expense with the given ID."
        body {
            required = true
            json {
                schema(
                    typeOf<List<Expense>>(),
                    listOf(
                        Expense(
                            id = "1",
                            category = "1",
                            expenseDate = "2021-01-10",
                            creationDate = "2021-01-10",
                            updatedDate = null,
                            deletedDate = null,
                            description = "Groceries",
                            amount = 1.23f,
                        )
                    )
                )
            }
        }
        200 response {
            description = "The expense was updated successfully."
        }
        400 response {
            description = "Invalid changes requested!"
        }
        401 response {
            description = "Not authorized!"
        }
        404 response {
            description = "Expense not found!"
        }
        405 response {
            description = "Could not validate changes!"
        }
        500 response {
            description = "Internal Server Error"
        }
    }
}
