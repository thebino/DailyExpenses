struct DailyExpensesApplication: App {

    // KMM - Koin Call
    init() {
        HelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
