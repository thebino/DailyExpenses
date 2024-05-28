package pro.stuermer.dailyexpenses.ui.settings

data class SettingsUiState(
    val isMaterialYouEnabled: Boolean = false,
    val showShareDialog: Boolean = false,
    val isSharingLoading: Boolean = false,
    val sharingError: String? = null,
    val isShareingEnrolled: Boolean = false,
    val sharingCode: String = "",
)
