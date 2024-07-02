package pro.stuermer.dailyexpenses.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.stuermer.dailyexpenses.data.repository.ExpensesRepository
import pro.stuermer.dailyexpenses.domain.Expense

class LoadLastTransactionsUseCase : KoinComponent {
    private val repository: ExpensesRepository by inject()

    suspend operator fun invoke(): Flow<List<Expense>> = repository.getExpenses().take(10)
}
