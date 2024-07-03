package pro.stuermer.dailyexpenses.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.byUnicodePattern
import pro.stuermer.dailyexpenses.data.persistence.ExpenseEntity as PersistedExpense
import pro.stuermer.dailyexpenses.domain.Expense as DomainExpense
import pro.stuermer.dailyexpenses.shared.Expense as NetworkExpense

object ExpenseMapper {
    internal var formatter = LocalDateTime.Format {byUnicodePattern("yyyy-MM-dd HH:mm")}

    fun toDomain(expense: NetworkExpense) = DomainExpense(
        id = expense.id,
        category = expense.category,
        expenseDate = LocalDateTime.parse(expense.expenseDate, formatter).date,
        creationDate = LocalDateTime.parse(expense.creationDate, formatter),
        updatedDate = expense.updatedDate?.let { LocalDateTime.parse(it, formatter) },
        deletedDate = expense.deletedDate?.let { LocalDateTime.parse(it, formatter) },
        description = expense.description,
        amount = expense.amount,
    )

    fun toDomain(expense: PersistedExpense) = DomainExpense(
        id = expense.identifier,
        category = expense.category,
        expenseDate = LocalDateTime.parse(expense.expenseDate, formatter).date,
        creationDate = LocalDateTime.parse(expense.creationDate, formatter),
        updatedDate = expense.updatedDate?.let { LocalDateTime.parse(it, formatter) },
        deletedDate = expense.deletedDate?.let { LocalDateTime.parse(it, formatter) },
        description = expense.description,
        amount = expense.amount,
    )

    fun toNetwork(expense: PersistedExpense) = NetworkExpense(
        id = expense.identifier,
        category = expense.category,
        expenseDate = expense.expenseDate,
        creationDate = expense.creationDate,
        updatedDate = expense.updatedDate,
        deletedDate = expense.deletedDate,
        description = expense.description,
        amount = expense.amount
    )

    fun toPersistenceModel(expense: DomainExpense) = PersistedExpense(
        identifier = expense.id,
        category = expense.category,
        expenseDate = expense.expenseDate.toString(),
        creationDate = expense.creationDate.toString(),
        updatedDate = expense.updatedDate?.toString(),
        deletedDate = expense.deletedDate?.toString(),
        description = expense.description,
        amount = expense.amount,
    )

    fun toPersistenceModel(expense: NetworkExpense) = PersistedExpense(
        identifier = expense.id,
        category = expense.category,
        expenseDate = expense.expenseDate,
        creationDate = expense.creationDate,
        updatedDate = expense.updatedDate,
        deletedDate = expense.deletedDate,
        description = expense.description,
        amount = expense.amount,
    )
}
