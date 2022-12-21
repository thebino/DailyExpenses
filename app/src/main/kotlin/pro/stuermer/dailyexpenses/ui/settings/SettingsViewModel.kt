package pro.stuermer.dailyexpenses.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.stuermer.dailyexpenses.data.network.Resource
import pro.stuermer.dailyexpenses.data.network.asResource
import pro.stuermer.dailyexpenses.domain.usecase.CreateSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.JoinSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.LeaveSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.StartSyncUseCase

class SettingsViewModel(
    private val getSharingUseCase: GetSharingUseCase,
    private val createSharingUseCase: CreateSharingUseCase,
    private val joinSharingUseCase: JoinSharingUseCase,
    private val leaveSharingUseCase: LeaveSharingUseCase,
    private val startSyncUseCase: StartSyncUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(SettingsUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadSharing()
        }
    }

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.EnableMaterialYou -> {
                // TODO: enable material you colors in theme
                // uiState.update { it.copy(isMaterialYouEnabled = true) }
            }

            SettingsEvent.DisableMaterialYou -> {
                // TODO: disable material you colors in theme
                //uiState.update { it.copy(isMaterialYouEnabled = false) }
            }

            SettingsEvent.ShowShareDialog -> {
                uiState.update { it.copy(showShareDialog = true) }
            }

            SettingsEvent.HideShareDialog -> {
                uiState.update { it.copy(showShareDialog = false) }
            }

            SettingsEvent.GenerateShare -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uiState.update { it.copy(isSharingLoading = true, sharingError = null) }
                    val result = createSharingUseCase()
                    result.onSuccess { sharingCode: String ->
                        startSyncUseCase()
                        uiState.update { it.copy(
                            isSharingLoading = false,
                            sharingCode = sharingCode,
                            sharingError = null
                        ) }
                    }

                    result.onFailure {
                        uiState.update { it.copy(
                            isSharingLoading = false,
                            sharingError = "Could not create sharing group!"
                        ) }
                    }
                }
            }

            SettingsEvent.LeaveSharing -> {
                uiState.update {
                    it.copy(
                        sharingCode = "", isShareingEnrolled = false
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    leaveSharingUseCase()
                }
            }

            is SettingsEvent.JoinShare -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uiState.update { it.copy(isSharingLoading = true, sharingError = null) }

                    val result = joinSharingUseCase(event.code)
                    result.onSuccess {
                        if (it) {
                            startSyncUseCase()
                            uiState.update {
                                it.copy(
                                    isSharingLoading = false,
                                    sharingError = null
                                )
                            }
                        } else {
                            uiState.update { it.copy(
                                isSharingLoading = false,
                                sharingError = "Could not join sharing group!"
                            ) }
                        }
                    }

                    result.onFailure {
                        uiState.update { it.copy(
                            isSharingLoading = false,
                            sharingError = "Could not join sharing group!"
                        ) }
                    }
                }
            }

            is SettingsEvent.CodeChanged -> {
                uiState.update { it.copy(sharingCode = event.code) }
            }
        }
    }

    private suspend fun loadSharing() {
        getSharingUseCase().asResource().collect { result ->
            when (result) {
                is Resource.Error -> {
                    // TODO: show error?
                    uiState.update { it.copy(isSharingLoading = false) }
                }

                is Resource.Loading -> {
                    uiState.update { it.copy(isSharingLoading = true) }
                }

                is Resource.Success -> {
                    uiState.update {
                        it.copy(
                            sharingCode = result.data.firstOrNull()?.code ?: "",
                            isSharingLoading = false,
                            isShareingEnrolled = result.data.isNotEmpty(),
                        )
                    }
                }
            }
        }
    }
}
