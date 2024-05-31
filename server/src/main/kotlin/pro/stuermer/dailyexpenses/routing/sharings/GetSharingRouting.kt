package pro.stuermer.dailyexpenses.routing.sharings

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import pro.stuermer.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.getSharingRouting(repository: DailyExpensesRepository) {
    get("sharing") {
        val code: String = call.request.queryParameters["code"] ?: ""
        call.application.environment.log.info("requested code to join: $code")

        if (code.length != 6) {
            call.respond(HttpStatusCode.BadRequest)
        }

        if (!code.all { char -> char.isLetterOrDigit() }) {
            call.respond(HttpStatusCode.NotAcceptable)
        }

        val instance = repository.getInstances(code)
        if (instance != null) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NoContent)
        }
    } describe {
        tags += "dailyexpense"
        summary = "Request to joins an existing sharing group."
        "code" queryParameter {
            description = "The sharing group code to join"
            required = true
        }
        200 response {
            description = "The request to join got granted."
        }
        204 response {
            description = "No sharing group with the given code was found!"
        }
        400 response {
            description = "The given sharing group code is invalid!"
        }
        406 response {
            description = "The given sharing group code has an invalid format!"
        }
    }
}
