package pro.stuermer.dailyexpenses.routing.frontend

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import pro.stuermer.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.getExpenses(repository: DailyExpensesRepository) {
    get("/list") {
        call.respond("TODO: list expenses")
    }
}
