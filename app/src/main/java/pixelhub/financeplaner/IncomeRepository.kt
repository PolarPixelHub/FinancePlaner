package pixelhub.financeplaner

import android.content.Context


class IncomeRepository(context: Context) {

    private val incomeDao = DatabaseProvider.getDatabase(context).incomeDao()

    // Insert function to add new income entry
    suspend fun addIncome(income: IncomeEntry) {
        incomeDao.insertIncome(income)
    }

    suspend fun getAllIncome(): List<IncomeEntry> {
        return incomeDao.getAllIncome()
    }

    suspend fun getAllIncomeTypes(): List<String> {
        return incomeDao.getAllIncomeTypes()
    }

}

