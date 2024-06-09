package pro.stuermer.dailyexpenses.data.persistence

import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
//@SmallTest
class ExpensesDaoTests {

    private lateinit var dao: ExpensesDao
    private lateinit var db: ExpensesDatabase

    @Before
    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(context, ExpensesDatabase::class.java)
//            .allowMainThreadQueries()
//            .build()
//
//        dao = db.expensesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
//        db.close()
    }

    @Test
    fun expensesDao_should_only_return_filtered_entries() = runBlocking {
//        // given
//        val allEntries = listOf(fakeDataExpense1, fakeDataExpense2, fakeDataExpense3)
//        dao.insert(*allEntries.toTypedArray())
//
//        // when
//        val date: LocalDate = LocalDate.parse("2022-02-01")
//        val monthstart: LocalDate = LocalDate.of(date.year, date.month,1)
//        val monthend: LocalDate = monthstart.plusDays(monthstart.lengthOfMonth() - 1L)
//        val todayEntries = dao.getExpensesForDate(
//            fromDate = monthstart,
//            toDate = monthend
//        ).first()
//
//        assertEquals(expected = listOf(fakeDataExpense2), todayEntries)
    }
}
