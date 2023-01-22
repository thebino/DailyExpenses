package pro.stuermer.dailyexpenses.data.model

sealed interface SyncStatus {
    object SyncSucceeded : SyncStatus
    object SyncSkipped : SyncStatus
    class SyncFailed(val message: String) : SyncStatus
}
