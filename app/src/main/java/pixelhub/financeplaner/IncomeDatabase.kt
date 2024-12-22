package pixelhub.financeplaner

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IncomeEntry::class], version = 1)
abstract class IncomeDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
}
