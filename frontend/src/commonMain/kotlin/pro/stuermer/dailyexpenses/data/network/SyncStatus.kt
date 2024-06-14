package pro.stuermer.dailyexpenses.data.network

sealed interface SyncStatus {
    object SyncSucceeded : SyncStatus
    object SyncSkipped : SyncStatus
    class SyncFailed(val message: String) : SyncStatus
}
