package pixelhub.financeplaner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: IncomeEntry)

    @Query("SELECT * FROM income ORDER BY date DESC")
    suspend fun getAllIncome(): List<IncomeEntry>

    // Fetch distinct types from IncomeEntity
    @Query("SELECT DISTINCT type FROM income")
    suspend fun getAllIncomeTypes(): List<String>
}
