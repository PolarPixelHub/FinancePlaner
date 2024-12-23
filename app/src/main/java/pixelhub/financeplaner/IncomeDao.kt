package pixelhub.financeplaner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: IncomeEntity)

    @Query("SELECT * FROM income ORDER BY date DESC")
    suspend fun getAllIncomes(): List<IncomeEntity>

}