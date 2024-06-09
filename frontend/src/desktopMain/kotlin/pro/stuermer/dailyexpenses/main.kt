import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import pro.stuermer.dailyexpenses.AppUi

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AppUi()
    }
}
