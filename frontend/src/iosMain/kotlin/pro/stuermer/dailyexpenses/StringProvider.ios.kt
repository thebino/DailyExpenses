package pro.stuermer.dailyexpenses.ui

actual fun string(id: String): String {
    return id
}

actual fun string(id: String, vararg args: Any): String {
    return id
}

actual fun plural(id: String, quantity: Int): String {
    return id
}

actual fun plural(id: String, quantity: Int, vararg args: Any): String {
    return id
}
