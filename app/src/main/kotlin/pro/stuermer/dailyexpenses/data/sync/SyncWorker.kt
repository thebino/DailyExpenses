package pro.stuermer.dailyexpenses.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import timber.log.Timber

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {
    private val repository: ExpensesRepository by inject()

    override suspend fun doWork(): Result {
        Timber.i("+++ do work +++")
        val result = repository.sync()

        return if (result) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    companion object {
        fun startUpSyncWork(applicationContext: Context) {
            WorkManager.getInstance(applicationContext).apply {
                enqueue(
                    OneTimeWorkRequest.from(SyncWorker::class.java)
                )
            }
        }
    }
}
