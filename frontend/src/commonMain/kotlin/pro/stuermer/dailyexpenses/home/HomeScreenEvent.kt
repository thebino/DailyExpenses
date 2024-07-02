package pro.stuermer.dailyexpenses.home

sealed interface HomeScreenEvent {
    data object RefreshEvent : HomeScreenEvent
}
