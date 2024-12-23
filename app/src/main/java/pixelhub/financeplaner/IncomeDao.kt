package pixelhub.financeplaner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: IncomeEntity)

    @Query("SELECT * FROM income ORDER BY date DESC")
    suspend fun getAllIncomes(): List<IncomeEntity>

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)

    @Update
    suspend fun editIncome(income: IncomeEntity)
}