package pro.stuermer.dailyexpenses.ui.settings

import pro.stuermer.dailyexpenses.ui.home.HomeScreenEvent

sealed interface SettingsEvent {
    object EnableMaterialYou: SettingsEvent
    object DisableMaterialYou: SettingsEvent

    object ShowShareDialog : SettingsEvent
    object HideShareDialog : SettingsEvent
    object GenerateShare : SettingsEvent
    object LeaveSharing : SettingsEvent
    class JoinShare(val code: String) : SettingsEvent
    class CodeChanged(val code: String) : SettingsEvent
}
