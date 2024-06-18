package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {
    @Query("SELECT * FROM expenses WHERE expenseDate >= :fromDate and expenseDate <= :toDate")
    fun getExpensesForDate(fromDate: String, toDate: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entries: ExpenseEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg expenseEntity: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE identifier IN (:ids)")
    suspend fun delete(ids: List<String>)

    @Query("DELETE FROM expenses")
    fun deleteAll()
}
