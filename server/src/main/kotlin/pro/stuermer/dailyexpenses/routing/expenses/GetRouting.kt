package pro.stuermer.dailyexpenses.routing.expenses

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.time.LocalDate
import pro.stuermer.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.getIndexRouting(repository: DailyExpensesRepository) {
    // get expenses (ALL or filtered)
    get {
        val instance = call.principal<UserIdPrincipal>()
        if (instance == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        val year: Int? = call.request.queryParameters["year"]?.toInt()
        val month: Int? = call.request.queryParameters["month"]?.toInt()

        if (year != null && month != null) {
            val isTooOld = year < 2000
            val isFutureYear = year > LocalDate.now().year
            val isFutureMonth = year == LocalDate.now().year && month > LocalDate.now().monthValue
            if (isTooOld || isFutureYear || isFutureMonth) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                call.respond(status = HttpStatusCode.OK, repository.getExpenseForDate(instance = instance.name, year = year, month = month))
            }
        } else {
            call.respond(status = HttpStatusCode.OK, repository.getExpenses(instance = instance.name))
        }
    } describe {
        tags += "dailyexpense"
        summary = "List all or filtered expenses."
        "year" queryParameter {
            description = "The year to filter expenses for."
            required = false
        }
        "month" queryParameter {
            description = "The month to filter expenses for."
            required = false
        }
        200 response {
            description = "The requested expenses."
        }
        400 response {
            description = "The given year or month to filter is invalid!"
        }
        401 response {
            description = "Not authorized!"
        }
        security("expenses-basic")
    }
}
