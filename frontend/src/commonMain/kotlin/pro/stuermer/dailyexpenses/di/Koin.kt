package pro.stuermer.dailyexpenses.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.ExpensesApiImpl
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDao
import pro.stuermer.dailyexpenses.data.persistence.ExpensesDatabase
import pro.stuermer.dailyexpenses.data.persistence.SharingDao
import pro.stuermer.dailyexpenses.data.persistence.SharingDatabase
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.data.repository.SharingsRepository

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule)
}

// called by iOS etc
fun initKoin() = initKoin {}

val commonModule = module {
    // Expenses
    single<ExpensesRepository> { ExpensesRepository.Default() }
    single<ExpensesApi> { ExpensesApiImpl() }
    factory<ExpensesDao> {
        provideExpensesDao(
            database = get()
        )
    }

    // Sharing
    single<SharingsRepository> { SharingsRepository.Default() }
    single {
        provideSharingDao(
            database = get()
        )
    }
    factory<SharingDao> {
        provideSharingDao(
            database = get()
        )
    }
}

private fun provideSharingDao(database: SharingDatabase): SharingDao {
    return database.sharingDao()
}

private fun provideExpensesDao(database: ExpensesDatabase): ExpensesDao {
    return database.expensesDao()
}
