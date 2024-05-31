package pro.stuermer.dailyexpenses.routing.sharings

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlin.random.Random
import pro.stuermer.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.postSharingRouting(repository: DailyExpensesRepository) {
    post("sharing") {
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        val code = (1..6).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")

        call.application.environment.log.info("create new sharing group with '$code'")

        val instance = repository.addInstances(code)
        if (instance != null) {
            call.response.headers.append(HttpHeaders.Location, "/api/sharing?code=$code")
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(status = HttpStatusCode.InternalServerError, "Could not create new sharing group")
        }
    } describe {
        tags += "dailyexpense"
        summary = "Create a new sharing group."
        description = "Start a new group to share the expenses with."

        201 response {
            description = "Find the code for the new sharing group in the Location header"
            // TODO: add Location header to documentation
        }
    }
}
