package pro.stuermer.dailyexpenses

expect fun string(id: String): String

expect fun string(id: String, vararg args: Any): String

expect fun plural(id: String, quantity: Int): String

expect fun plural(id: String, quantity: Int, vararg args: Any): String
