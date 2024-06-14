package pro.stuermer.dailyexpenses

import dailyexpenses.frontend.generated.resources.Res
import dailyexpenses.frontend.generated.resources.title_home
import dailyexpenses.frontend.generated.resources.title_statistics
import dailyexpenses.frontend.generated.resources.title_history
import dailyexpenses.frontend.generated.resources.title_settings
import org.jetbrains.compose.resources.StringResource

enum class Destinations(val title: StringResource) {
    Home(title = Res.string.title_home),
    Statistics(title = Res.string.title_statistics),
    History(title = Res.string.title_history),
    Settings(title = Res.string.title_settings),
}
