package pro.stuermer.dailyexpenses.data.network

sealed interface SyncStatus {
    data object SyncSucceeded : SyncStatus
    data object SyncSkipped : SyncStatus
    class SyncFailed(val message: String) : SyncStatus
}
