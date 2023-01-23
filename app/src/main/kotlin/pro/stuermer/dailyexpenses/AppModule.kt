package pro.stuermer.dailyexpenses

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import androidx.room.Room
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.security.KeyStore
import java.util.UUID
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.data.persistence.SharingDatabase
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepositoryImpl
import pro.stuermer.dailyexpenses.data.repository.SharingsRepository
import pro.stuermer.dailyexpenses.data.repository.SharingsRepositoryImpl
import pro.stuermer.dailyexpenses.data.sync.SyncWorker
import pro.stuermer.dailyexpenses.domain.usecase.AddExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.CreateSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.DeleteExpenseUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetExpensesForDateUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetExpensesUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.JoinSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.LeaveSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.StartSyncUseCase
import pro.stuermer.dailyexpenses.domain.usecase.UpdateExpenseUseCase
import pro.stuermer.dailyexpenses.ui.home.HomeViewModel
import pro.stuermer.dailyexpenses.ui.settings.SettingsViewModel


val appModule = module {
    // ui
    viewModel {
        HomeViewModel(
            getExpensesForDateUseCase = get(),
            addExpenseUseCase = get(),
            updateExpenseUseCase = get(),
            deleteExpenseUseCase = get(),
        )
    }
    viewModel {
        SettingsViewModel(
            getSharingUseCase = get(),
            createSharingUseCase = get(),
            joinSharingUseCase = get(),
            leaveSharingUseCase = get(),
            startSyncUseCase = get(),
        )
    }


    // domain
    factory {
        StartSyncUseCase(
            applicationContext = get()
        )
    }
    factory {
        GetExpensesUseCase(
            repository = get()
        )
    }
    factory {
        GetExpensesForDateUseCase(
            repository = get()
        )
    }
    factory {
        AddExpenseUseCase(
            repository = get()
        )
    }
    factory {
        UpdateExpenseUseCase(
            repository = get()
        )
    }
    factory {
        DeleteExpenseUseCase(
            repository = get()
        )
    }
    factory {
        GetSharingUseCase(
            repository = get()
        )
    }
    factory {
        CreateSharingUseCase(
            repository = get()
        )
    }
    factory {
        JoinSharingUseCase(
            repository = get()
        )
    }
    factory {
        LeaveSharingUseCase(
            repository = get()
        )
    }


    // data
    worker {
        SyncWorker(
            get(),
            get(),
        )
    }

    factory<ExpensesApi> {
        ExpensesApi.Default(httpClient = get())
    }

    single<HttpClient> {
        provideHttpClient(application = get())
    }

    single<SharingDatabase> {
        provideSharingsDatabase(
            context = get()
        )
    }

    factory<SharingDao> {
        provideSharingDao(
            database = get()
        )
    }

    single<ExpensesDatabase> {
        provideExpensesDatabase(
            context = get()
        )
    }
    factory<ExpensesDao> {
        provideExpensesDao(
            database = get()
        )
    }

    single<ExpensesRepository> {
        ExpensesRepositoryImpl(
            api = get(),
            dao = get(),
            sharingDao = get(),
        )
    }
    single<SharingsRepository> {
        SharingsRepositoryImpl(
            api = get(), dao = get()
        )
    }
}

private fun provideSharingsDatabase(context: Context): SharingDatabase {
    return Room.databaseBuilder(
        context, SharingDatabase::class.java, "sharings.db"
    ).build()
}

private fun provideSharingDao(database: SharingDatabase): SharingDao {
    return database.sharingsDao()
}

private fun provideExpensesDatabase(context: Context): ExpensesDatabase {
    return Room.databaseBuilder(
        context, ExpensesDatabase::class.java, "expenses.db"
    )
        .build()
}

private fun provideExpensesDao(database: ExpensesDatabase): ExpensesDao {
    return database.expensesDao()
}


private fun provideHttpClient(application: Application): HttpClient {
    val sslContext = buildClientCertificateAuthenticationContext(application = application)

    return HttpClient(Android) {
        expectSuccess = false
        followRedirects = true

        defaultRequest {
            url(BuildConfig.API_URL)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            headers {
                val context = application.applicationContext

                @Suppress("DEPRECATION")
                val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val version: String = packageInfo.versionName

                @Suppress("DEPRECATION")
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    packageInfo.versionCode
                }

                append(
                    "User-Agent",
                    "dailyExpenses-Android/$version (Build $versionCode) Android/${Build.VERSION.RELEASE}"
                )
            }
        }

        @Suppress("MagicNumber")
        engine {
            threadsCount = 1_000
            sslManager = { httpsURLConnection ->
                httpsURLConnection.sslSocketFactory = sslContext.socketFactory
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }, contentType = ContentType.Application.Json
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    // logcat(LogPriority.VERBOSE) { message }
                }
            }
            level = LogLevel.ALL
        }
    }
}

private fun buildClientCertificateAuthenticationContext(
    application: Application
): SSLContext {
    val certificateStream = application.assets.open(BuildConfig.CLIENT_CERT_FILE)

    val keyStore = KeyStore.getInstance("PKCS12").apply {
        load(certificateStream, BuildConfig.CLIENT_CERT_PASSWORD.toCharArray())
    }

    val kmf = KeyManagerFactory.getInstance("X509")
    kmf.init(keyStore, BuildConfig.CLIENT_CERT_PASSWORD.toCharArray())
    val keyManagers = kmf.keyManagers

    val context = SSLContext.getInstance("TLS")
    context.init(keyManagers, null, null)
    return context
}
