package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import pro.stuermer.dailyexpenses.data.persistence.model.Expense

@Dao
interface ExpensesDao {
    @Query("SELECT * FROM expenses WHERE expenseDate >= :fromDate and expenseDate <= :toDate")
    fun getExpensesForDate(fromDate: LocalDate, toDate: LocalDate): Flow<List<Expense>>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entries: Expense)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg expense: Expense)

    @Query("DELETE FROM expenses WHERE identifier IN (:ids)")
    suspend fun delete(ids: List<String>)

    @Query("DELETE FROM expenses")
    fun deleteAll()
}
