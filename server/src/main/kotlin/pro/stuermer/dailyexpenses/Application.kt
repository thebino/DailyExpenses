package pro.stuermer.dailyexpenses

import io.ktor.server.routing.*
import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.TegralOpenApiKtor
import guru.zoroark.tegral.openapi.ktor.describe
import guru.zoroark.tegral.openapi.ktor.openApiEndpoint
import guru.zoroark.tegral.openapi.ktorui.TegralSwaggerUiKtor
import guru.zoroark.tegral.openapi.ktorui.swaggerUiEndpoint
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.plugin
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.http.content.file
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File
import kotlin.reflect.typeOf
import kotlin.time.Duration.Companion.seconds
import pro.stuermer.balloon.dailyexpenses.data.persistence.model.Instance
import pro.stuermer.balloon.dailyexpenses.data.repository.DailyExpensesRepository
import pro.stuermer.balloon.dailyexpenses.data.repository.DailyExpensesRepositoryImpl
import pro.stuermer.balloon.dailyexpenses.routing.deleteIndexRouting
import pro.stuermer.balloon.dailyexpenses.routing.frontend.getExpenses
import pro.stuermer.balloon.dailyexpenses.routing.frontend.getInvite
import pro.stuermer.balloon.dailyexpenses.routing.getCategoriesRouting
import pro.stuermer.balloon.dailyexpenses.routing.getIndexRouting
import pro.stuermer.balloon.dailyexpenses.routing.getSharingRouting
import pro.stuermer.balloon.dailyexpenses.routing.postIndexRouting
import pro.stuermer.balloon.dailyexpenses.routing.postSharingRouting
import pro.stuermer.balloon.dailyexpenses.routing.putIndexRouting

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.mainModule() {


    // automatic openapi and swagger documentation
    install(TegralOpenApiKtor) {
        title = "Balloon"
        version = "1.4.0"
        description = """
                This server provides endpoints for the [Daily Expenses](https://github.com/thebino/dailyexpenses) mobile application.
            """.trimIndent()
        summary = "Summary"
        licenseName = "Apache 2.0"
        licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html"
        licenseIdentifier = "Apache 2.0"
        "http://127.0.0.1:8080" server {}
        "dailyexpense" tag {
            description = "Everything for the daily expenses application"
            externalDocsDescription = "Daily expenses"
            externalDocsUrl = "https://github.com/thebino/dailyexpenses"
        }
    }
    install(TegralSwaggerUiKtor)

    install(Compression) {
        gzip {
            priority = 0.9
        }
        deflate {
            priority = 1.0
        }
    }

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging)

    // Automatic '304 Not Modified' Responses
    install(ConditionalHeaders)

    // limit access to specific hosts
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    // add a rate limit to reduce attacks
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 60.seconds)
        }
    }

    // add custom server heades
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "Balloon/1.4.0")
    }

    install(Authentication) {
        // authentication provider are added in modules
        basic(name = "auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (credentials.name == "swagger" && credentials.password == "expenses") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    routing {
        authenticate("auth-basic") {
            openApiEndpoint("/openapi")
            swaggerUiEndpoint(path = "/swagger", openApiPath = "/openapi")
        }

        static("/") {
            staticRootFolder = File("static")
            static(".well-known") {
                file("assetlinks.json")
            }
        }

        get("/") {
            call.respond(VersionInfo("1.4.0"))
        } describe {
            tags += "general"
            summary = "Returns the application version."
            description =
                "Default endpoint to check if the application is up and running also getting the running version number."
            200 response {
                description = "The operation was successful"
                json {
                    schema(typeOf<VersionInfo>(), VersionInfo("1.4.0"))
                }
            }
        }
    }
}

fun Application.expensesModule(
    testing: Boolean = false,
    installAuth: Boolean = true
) {
    val host = environment.config.propertyOrNull("ktor.application.database.host")?.getString()
    val port = environment.config.propertyOrNull("ktor.application.database.port")?.getString()?.toInt()
    val user = environment.config.propertyOrNull("ktor.application.database.user")?.getString()
    val pass = environment.config.propertyOrNull("ktor.application.database.pass")?.getString()
    val table = environment.config.propertyOrNull("ktor.application.database.table")?.getString()
    val repository: DailyExpensesRepository = DailyExpensesRepositoryImpl(
        testing = testing,
        host = host ?: "localhost",
        port = port ?: 3006,
        user = user ?: "balloon",
        pass = pass ?: "balloon",
        table = table ?: "balloon"
    )

    if (installAuth) {
        plugin(Authentication).configure {
            basic("expenses-basic") {
                realm = "Access to daily expenses"
                validate { credentials ->
                    val instance: Instance? = repository.getInstances(credentials.name)

                    if (instance != null) {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }
    }

    routing {
        // frontend for DailyExpense
        route("/expense") {
            getInvite(repository)
            authenticate("expenses-basic") {
                getExpenses(repository)
            }
        }

        // all requests below /api are restricted to ssl certificate authentication by a reverse proxy (nginx)
        route("/api") {
            // daily expense api
            route("/expense") {
                // POST /sharing
                postSharingRouting(repository)

                // GET /sharing
                getSharingRouting(repository)

                authenticate("expenses-basic") {
                    // GET /
                    getIndexRouting(repository)

                    // POST /
                    postIndexRouting(repository)

                    // PUT /
                    putIndexRouting(repository)

                    // DELETE /
                    deleteIndexRouting(repository)

                    // GET /categories
                    getCategoriesRouting(repository)
                }
            }
        }
    }
}
