import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(pro.stuermer.dailyexpenses.di.commonModule)
    }
}
