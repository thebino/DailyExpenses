package pro.stuermer.dailyexpenses.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pro.stuermer.dailyexpenses.data.network.ExpensesApi
import pro.stuermer.dailyexpenses.data.network.ExpensesApiImpl
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule)
}

// called by iOS etc
fun initKoin() = initKoin{}

val commonModule = module {
    single { ExpensesRepository() }
    single<ExpensesApi> { ExpensesApiImpl() }
}
