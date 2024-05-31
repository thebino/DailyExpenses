package pro.stuermer.dailyexpenses.data.repository

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import pro.stuermer.dailyexpenses.data.persistence.model.ExpensesTable
import pro.stuermer.balloon.dailyexpenses.data.persistence.model.Instance
import pro.stuermer.balloon.dailyexpenses.data.persistence.model.Instances
import pro.stuermer.dailyexpenses.Expense as NetworkExpense

class DailyExpensesRepositoryImpl(
    testing: Boolean = false,
    host: String = "database",
    port: Int = 3006,
    user: String = "balloon",
    pass: String = "balloon",
    table: String = "balloon"
) : DailyExpensesRepository {
    init {
        val remote = false
        val driverClassName = if (testing) {
            "org.h2.Driver"
        } else {
            if (remote) {
                "com.mysql.cj.jdbc.Driver"
            } else {
                "org.sqlite.JDBC"
            }
        }
        val jdbcURL = if (testing) {
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;"
        } else {
            if (remote) {
                "jdbc:mysql://$host:$port/$table"
            } else {
                "jdbc:sqlite:./database.db"

            }
        }
        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = user,
            password = pass
        )

        transaction(database) {
            SchemaUtils.create(ExpensesTable)
            SchemaUtils.create(Instances)
        }
    }

    override suspend fun addInstances(code: String): Instance? {
        return newSuspendedTransaction(Dispatchers.IO) {
            val insertStatement = Instances.insert { it[Instances.code] = code }

            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToInstance)
        }
    }

    override suspend fun getInstances(code: String): Instance? {
        return newSuspendedTransaction(Dispatchers.IO) {
            val query = Instances.select { Instances.code eq code }

            query.map(::resultRowToInstance).firstOrNull()
        }
    }

    override suspend fun addNewExpense(
        instance: String,
        id: String,
        category: String,
        expenseDate: String,
        creationDate: String,
        updatedDate: String?,
        description: String,
        amount: Float
    ): NetworkExpense? {
        return newSuspendedTransaction(Dispatchers.IO) {
            val localExpenseDate: LocalDate = LocalDate.parse(expenseDate)

            @Suppress("RemoveRedundantQualifierName")
            val insertStatement = ExpensesTable.insert {
                it[ExpensesTable.id] = id
                it[ExpensesTable.instance] = instance
                it[ExpensesTable.category] = category
                it[ExpensesTable.year] = localExpenseDate.year
                it[ExpensesTable.month] = localExpenseDate.month.value
                it[ExpensesTable.day] = localExpenseDate.dayOfMonth
                it[ExpensesTable.creationDate] = creationDate
                it[ExpensesTable.updatedDate] = updatedDate
                it[ExpensesTable.description] = description
                it[ExpensesTable.amount] = amount
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToExpense)
        }
    }

    override suspend fun addExpenses(instance: String, expenses: List<NetworkExpense>) {
        // iterate through expenses
        expenses.forEach { remoteExpense: NetworkExpense ->
            val localExpense = getExpenseForId(instance, remoteExpense.id)

            when {
                localExpense == null -> {
                    // insert
                    addNewExpense(
                        id = remoteExpense.id,
                        instance = instance,
                        category = remoteExpense.category,
                        expenseDate = remoteExpense.expenseDate,
                        creationDate = remoteExpense.creationDate,
                        updatedDate = remoteExpense.updatedDate,
                        description = remoteExpense.description,
                        amount = remoteExpense.amount
                    )
                }

                remoteExpense.deletedDate != null -> {
                    // delete, remote got deleted
                    deleteExpenseWithID(expenseID = remoteExpense.id)
                }

                localExpense.updatedDate != null && remoteExpense.updatedDate == null -> {
                    // skip, local has changed
                }

                localExpense.updatedDate != null && remoteExpense.updatedDate != null && localExpense.updatedDate!!.toDateTime() < remoteExpense.updatedDate!!.toDateTime() -> {
                    // update, remote has changed
                    updateExpenseWithId(
                        instance = instance,
                        NetworkExpense(
                            id = remoteExpense.id,
                            category = remoteExpense.category,
                            expenseDate = remoteExpense.expenseDate,
                            creationDate = remoteExpense.creationDate,
                            description = remoteExpense.description,
                            amount = remoteExpense.amount
                        )
                    )
                }

                localExpense.updatedDate == null && remoteExpense.updatedDate != null -> {
                    // update, remote has changed
                    updateExpenseWithId(
                        instance = instance,
                        NetworkExpense(
                            id = remoteExpense.id,
                            category = remoteExpense.category,
                            expenseDate = remoteExpense.expenseDate,
                            creationDate = remoteExpense.creationDate,
                            description = remoteExpense.description,
                            amount = remoteExpense.amount
                        )
                    )
                }

                else -> {
                    // already up-to-date
                }
            }
        }
    }

    override suspend fun updateExpenseWithId(instance: String, expense: NetworkExpense): Int {
        val localExpenseDate: LocalDate = LocalDate.parse(expense.expenseDate)

        return newSuspendedTransaction(Dispatchers.IO) {
            @Suppress("RemoveRedundantQualifierName")
            val updatedExpenses = ExpensesTable.update({ ExpensesTable.id eq expense.id }) {
                it[ExpensesTable.category] = expense.category
                it[ExpensesTable.instance] = instance
                it[ExpensesTable.year] = localExpenseDate.year
                it[ExpensesTable.month] = localExpenseDate.month.value
                it[ExpensesTable.day] = localExpenseDate.dayOfMonth
                it[ExpensesTable.description] = expense.description
                it[ExpensesTable.amount] = expense.amount
                it[ExpensesTable.updatedDate] = expense.updatedDate
                it[ExpensesTable.creationDate] = expense.creationDate
                // deletedDate is only set in DELETE call
            }

            updatedExpenses
        }
    }

    override suspend fun deleteExpenseWithID(expenseID: String) {
        newSuspendedTransaction(Dispatchers.IO) {
            ExpensesTable.update({ ExpensesTable.id eq expenseID }) {
                it[ExpensesTable.deletedDate] = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
        }
    }

    override suspend fun getExpenses(instance: String): List<NetworkExpense> {
        return newSuspendedTransaction(Dispatchers.IO) {
            ExpensesTable.select { ExpensesTable.instance eq instance }.map(::resultRowToExpense)
        }
    }

    override suspend fun getExpenseForId(
        instance: String,
        id: String
    ): NetworkExpense? {
        return newSuspendedTransaction(Dispatchers.IO) {
            ExpensesTable.select { ExpensesTable.instance eq instance and (ExpensesTable.id eq id) }.limit(1)
                .map(::resultRowToExpense).firstOrNull()
        }
    }

    override suspend fun getExpenseForDate(instance: String, year: Int, month: Int): List<NetworkExpense> {
        return newSuspendedTransaction(Dispatchers.IO) {
            ExpensesTable.select { ExpensesTable.instance eq instance and (ExpensesTable.year eq year) and (ExpensesTable.month eq month) }
                .map(::resultRowToExpense)
        }
    }

    private fun resultRowToExpense(row: ResultRow) = NetworkExpense(
        id = row[ExpensesTable.id],
        category = row[ExpensesTable.category],
        expenseDate = "${row[ExpensesTable.year]}-${
            row[ExpensesTable.month].toString().padStart(2, '0')
        }-${row[ExpensesTable.day].toString().padStart(2, '0')}",
        creationDate = row[ExpensesTable.creationDate],
        updatedDate = row[ExpensesTable.updatedDate],
        description = row[ExpensesTable.description],
        deletedDate = row[ExpensesTable.deletedDate],
        amount = row[ExpensesTable.amount],
    )

    private fun resultRowToInstance(row: ResultRow?): Instance? {
        return row?.let {
            Instance(
                id = row[Instances.id],
                code = row[Instances.code]
            )
        }
    }
}

private fun String.toDateTime() = LocalDateTime.parse(this)

