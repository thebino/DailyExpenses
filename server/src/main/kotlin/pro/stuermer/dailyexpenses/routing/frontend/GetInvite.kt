package pro.stuermer.dailyexpenses.routing.frontend

import io.ktor.server.application.call
import io.ktor.server.request.userAgent
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import pro.stuermer.dailyexpenses.data.repository.DailyExpensesRepository

fun Route.getInvite(repository: DailyExpensesRepository) {
    get("/invite") {
        val inviteCode = call.request.queryParameters["code"]
        val userAgent = call.request.userAgent()


        val browserRegex = Regex("Edge?\\/(10[5-9]|1[1-9]\\d|[2-9]\\d\\d|\\d{4,})(\\.\\d+|)(\\.\\d+|)|Firefox\\/(10[4-9]|1[1-9]\\d|[2-9]\\d\\d|\\d{4,})\\.\\d+(\\.\\d+|)|Chrom(ium|e)\\/(10[5-9]|1[1-9]\\d|[2-9]\\d\\d|\\d{4,})\\.\\d+(\\.\\d+|)|Maci.* Version\\/(15\\.([6-9]|\\d{2,})|(1[6-9]|[2-9]\\d|\\d{3,})\\.\\d+)([,.]\\d+|)( Mobile\\/\\w+|) Safari\\/|Chrome.+OPR\\/(9\\d|\\d{3,})\\.\\d+\\.\\d+|(CPU[ +]OS|iPhone[ +]OS|CPU[ +]iPhone|CPU IPhone OS|CPU iPad OS)[ +]+(15[._]([6-9]|\\d{2,})|(1[6-9]|[2-9]\\d|\\d{3,})[._]\\d+)([._]\\d+|)|Opera Mini|Android:?[ /\\-](10[6-9]|1[1-9]\\d|[2-9]\\d{2}|\\d{4,})(\\.\\d+|)(\\.\\d+|)|Mobile Safari.+OPR\\/(6[4-9]|[7-9]\\d|\\d{3,})\\.\\d+\\.\\d+|Android.+Firefox\\/(10[5-9]|1[1-9]\\d|[2-9]\\d\\d|\\d{4,})\\.\\d+(\\.\\d+|)|Android.+Chrom(ium|e)\\/(10[6-9]|1[1-9]\\d|[2-9]\\d\\d|\\d{4,})\\.\\d+(\\.\\d+|)|Android.+(UC? ?Browser|UCWEB|U3)[ /]?(13\\.([4-9]|\\d{2,})|(1[4-9]|[2-9]\\d|\\d{3,})\\.\\d+)\\.\\d+|SamsungBrowser\\/(1[7-9]|[2-9]\\d|\\d{3,})\\.\\d+|Android.+MQQBrowser\\/(13(\\.([1-9]|\\d{2,})|)|(1[4-9]|[2-9]\\d|\\d{3,})(\\.\\d+|))(\\.\\d+|)|K[Aa][Ii]OS\\/(2\\.([5-9]|\\d{2,})|([3-9]|\\d{2,})\\.\\d+)(\\.\\d+|)")
        val browser = userAgent?.let { browserRegex.find(it)?.value } ?: "browser not-found"
        val osRegex = Regex("Linux; Android|Android|Macintosh|Linux|iPhone|Windows")
        val os = userAgent?.let { osRegex.find(it)?.value } ?: "OS not-found"


        // TODO: get platform via user-agent
        val isAndroid = os == "Android" || os == "Linux; Android"
        val isIos = os == "iPhone"

        println(os)
        println(browser)
        println(userAgent)

        if (isAndroid) {
            call.respondRedirect(
                url = "http://play.google.com/store/apps/details?id=pro.stuermer.dailyexpenses&launch=true&referrer=abc123",
                permanent = false
            )
        }
        if (isIos) {
            call.respondRedirect(
                url = "http://itunes.apple.com/lb/app/pro-stuermer-dailyexpenses/id1234567890",
                permanent = false
            )
        }

        call.respondRedirect(
            url = "/expense/list?code=abc123",
            permanent = false
        )
    }
}