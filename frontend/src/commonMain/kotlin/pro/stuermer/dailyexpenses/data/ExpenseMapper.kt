package pro.stuermer.dailyexpenses.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import pro.stuermer.dailyexpenses.data.persistence.ExpenseEntity as PersistedExpense
import pro.stuermer.dailyexpenses.domain.Expense as DomainExpense
import pro.stuermer.dailyexpenses.shared.Expense as NetworkExpense

object ExpenseMapper {
    internal var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun toDomain(expense: NetworkExpense) = DomainExpense(
        id = expense.id,
        category = expense.category,
        expenseDate = LocalDateTime.parse(expense.expenseDate, formatter).toLocalDate(),
        creationDate = LocalDateTime.parse(expense.creationDate, formatter),
        updatedDate = expense.updatedDate?.let { LocalDateTime.parse(it, formatter) },
        deletedDate = expense.deletedDate?.let { LocalDateTime.parse(it, formatter) },
        description = expense.description,
        amount = expense.amount,
    )

    fun toDomain(expense: PersistedExpense) = DomainExpense(
        id = expense.identifier,
        category = expense.category,
        expenseDate = LocalDateTime.parse(expense.expenseDate, formatter).toLocalDate(),
        creationDate = LocalDateTime.parse(expense.creationDate, formatter),
        updatedDate = expense.updatedDate?.let { LocalDateTime.parse(it, formatter) },
        deletedDate = expense.deletedDate?.let { LocalDateTime.parse(it, formatter) },
        description = expense.description,
        amount = expense.amount,
    )

    fun toNetwork(expense: PersistedExpense) = NetworkExpense(
        id = expense.identifier,
        category = expense.category,
        expenseDate = expense.expenseDate.format(formatter),
        creationDate = expense.creationDate.format(formatter),
        updatedDate = expense.updatedDate?.format(formatter),
        deletedDate = expense.deletedDate?.format(formatter),
        description = expense.description,
        amount = expense.amount
    )

    fun toPersistenceModel(expense: DomainExpense) = PersistedExpense(
        identifier = expense.id,
        category = expense.category,
        expenseDate = expense.expenseDate.format(formatter),
        creationDate = expense.creationDate.format(formatter),
        updatedDate = expense.updatedDate?.format(formatter),
        deletedDate = expense.deletedDate?.format(formatter),
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
