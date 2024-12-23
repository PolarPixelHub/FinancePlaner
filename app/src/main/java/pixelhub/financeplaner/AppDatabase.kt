package pixelhub.financeplaner

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IncomeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
}