package pro.stuermer.dailyexpenses.domain.usecase

import android.content.Context
import pro.stuermer.dailyexpenses.data.sync.SyncWorker

class StartSyncUseCase(
    private val applicationContext: Context
) {
    suspend operator fun invoke() {
        SyncWorker.startUpSyncWork(applicationContext)
    }
}