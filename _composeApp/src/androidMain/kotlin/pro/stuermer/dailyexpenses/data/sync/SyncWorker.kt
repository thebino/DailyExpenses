package pro.stuermer.dailyexpenses.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.model.SyncStatus
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {
    private val repository: ExpensesRepository by inject()

    /**
     * Start sync.
     */
    override suspend fun doWork(): Result {
        // Start sync inside the repository
        return when (repository.sync()) {
            is SyncStatus.SyncFailed -> Result.failure()
            SyncStatus.SyncSkipped -> Result.success()
            SyncStatus.SyncSucceeded -> Result.success()
        }
    }

    companion object {
        fun startUpSyncWork(applicationContext: Context) {
            WorkManager.getInstance(applicationContext).apply {
                enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
            }
        }
    }
}
