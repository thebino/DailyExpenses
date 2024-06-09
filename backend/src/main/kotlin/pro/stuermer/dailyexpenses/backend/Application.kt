package pro.stuermer.dailyexpenses.backend

import guru.zoroark.tegral.openapi.dsl.apiKeyType
import guru.zoroark.tegral.openapi.dsl.inHeader
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
import io.ktor.server.request.httpMethod
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import pro.stuermer.dailyexpenses.shared.VersionInfo
import java.io.File
import kotlin.reflect.typeOf
import kotlin.time.Duration.Companion.seconds
import pro.stuermer.dailyexpenses.backend.persistence.Instance
import pro.stuermer.dailyexpenses.backend.repository.DailyExpensesRepository
import pro.stuermer.dailyexpenses.backend.repository.DailyExpensesRepositoryImpl
import pro.stuermer.dailyexpenses.backend.routing.categories.getCategoriesRouting
import pro.stuermer.dailyexpenses.backend.routing.expenses.deleteIndexRouting
import pro.stuermer.dailyexpenses.backend.routing.expenses.getIndexRouting
import pro.stuermer.dailyexpenses.backend.routing.expenses.postIndexRouting
import pro.stuermer.dailyexpenses.backend.routing.expenses.putIndexRouting
import pro.stuermer.dailyexpenses.backend.routing.sharings.getSharingRouting
import pro.stuermer.dailyexpenses.backend.routing.sharings.postSharingRouting

const val AUTH_NAME_EXPENSES = "expenses-basic"
fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused", "MaximumLineLength", "MaxLineLength") // Referenced in application.conf
fun Application.mainModule() {
    // automatic openapi and swagger documentation
    install(TegralOpenApiKtor) {
        title = "DailyExpenses"
        version = "1.4.0"
        description =
            """This server provides endpoints for sharing expenses with the [Daily Expenses](https://github.com/thebino/dailyexpenses) mobile application.""".trimIndent()
        summary = "Summary"
        licenseName = "Apache 2.0"
        licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html"
        licenseIdentifier = "Apache 2.0"
        "http://127.0.0.1:8080" server {}
        "dailyexpense" tag {
            description =
                "Endpoints for sharing expenses with the Daily Expenses mobile application."
            externalDocsDescription = "Daily expenses"
            externalDocsUrl = "https://github.com/thebino/dailyexpenses"
        }
        "expenses-basic" securityScheme {
            apiKeyType
            inHeader
            name = "Authorization"
            description = "This is the description of my security scheme"
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

    install(CallLogging) {
        level = Level.DEBUG
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }

    // Automatic '304 Not Modified' Responses
    install(ConditionalHeaders)

    // limit access to specific hosts
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
    }

    // add a rate limit to reduce attacks
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 60.seconds)
        }
    }

    // add custom server heades
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "DailyExpenses/1.4.0")
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
            tags += "dailyexpense"
            summary = "Returns the current version."
            description =
                "Default endpoint to check if the server is up and running by returning the current version number."
            200 response {
                description = "The operation was successful."
                json {
                    schema(typeOf<VersionInfo>(), VersionInfo("1.2.3"))
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
    val port =
        environment.config.propertyOrNull("ktor.application.database.port")?.getString()?.toInt()
    val user = environment.config.propertyOrNull("ktor.application.database.user")?.getString()
    val pass = environment.config.propertyOrNull("ktor.application.database.pass")?.getString()
    val table = environment.config.propertyOrNull("ktor.application.database.table")?.getString()
    val repository: DailyExpensesRepository = DailyExpensesRepositoryImpl(
        testing = testing,
        host = host ?: "localhost",
        port = port ?: 3006,
        user = user ?: "expenses",
        pass = pass ?: "expenses",
        table = table ?: "expenses"
    )

    if (installAuth) {
        plugin(Authentication).configure {
            basic(AUTH_NAME_EXPENSES) {
                realm = "Access to daily expenses"
                validate { credentials ->
                    println("credentials: ${credentials.name}")
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
        route("/api") {
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
