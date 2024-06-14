package pro.stuermer.dailyexpenses

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import pro.stuermer.dailyexpenses.di.commonModule
import pro.stuermer.dailyexpenses.di.initKoin

class DailyExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(
                if (BuildConfig.DEBUG) {
                    Level.ERROR
                } else {
                    Level.NONE
                }
            )

            androidContext(applicationContext)

            modules(listOf(commonModule))
        }
    }
}
