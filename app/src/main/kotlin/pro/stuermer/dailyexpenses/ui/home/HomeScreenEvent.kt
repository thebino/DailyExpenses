package pro.stuermer.dailyexpenses.ui.home

import pro.stuermer.dailyexpenses.domain.model.Expense

sealed interface HomeScreenEvent {
    object SelectPreviousMonth : HomeScreenEvent
    object SelectNextMonth : HomeScreenEvent
    class AddEvent(val expense: Expense) : HomeScreenEvent
    class UpdateEvent(val expense: Expense) : HomeScreenEvent
    object NewItemEvent : HomeScreenEvent
    object HideInput : HomeScreenEvent
    class EditExpenseEvent(val expense: Expense) : HomeScreenEvent
    class DeleteExpenseEvenr(val expense: Expense) : HomeScreenEvent
}
