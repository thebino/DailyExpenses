package pro.stuermer.dailyexpenses.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.domain.usecase.LoadLastTransactionsUseCase

class HomeViewModel : ViewModel(), KoinComponent {
    private val loadLastTransactionsUseCase: LoadLastTransactionsUseCase by inject()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun handleEvent(homeScreenEvent: HomeScreenEvent) {
        when (homeScreenEvent) {
            HomeScreenEvent.RefreshEvent -> {
                _uiState.update {
                    it.copy(
                        loadingTransactions = true,
                        loadingAmount = true
                    )
                }
                viewModelScope.launch(Dispatchers.Default) {
                    loadLastTransactionsUseCase()
                }
            }
        }
    }
}
