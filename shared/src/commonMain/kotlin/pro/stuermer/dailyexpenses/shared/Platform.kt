package pro.stuermer.dailyexpenses.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
