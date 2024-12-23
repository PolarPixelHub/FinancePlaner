package pixelhub.financeplaner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncomeEntriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IncomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_entries)

        loadDatabase()
    }

    private fun loadDatabase() {
        recyclerView = findViewById(R.id.recycler_view_entries)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            val incomes = db.incomeDao().getAllIncomes() // Make sure to define this query in your DAO

            withContext(Dispatchers.Main) {
                adapter = IncomeAdapter(incomes) { income ->
                    showOptionsDialog(income)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun showOptionsDialog(income: IncomeEntity) {
        val options = arrayOf("Edit", "Delete", "Cancel")

        AlertDialog.Builder(this)
            .setTitle("Select Action")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editIncome(income)      // Bearbeiten
                    1 -> deleteIncome(income)    // Löschen
                    2 -> {}                     // Abbrechen
                }
            }
            .create()
            .show()
    }

    private fun editIncome(income: IncomeEntity) {
        // Logik zum Bearbeiten des Einkommens (z.B. Activity oder Dialog aufrufen)
        Toast.makeText(this, "Edit Income: ${income.type}", Toast.LENGTH_SHORT).show()
    }

    private fun deleteIncome(income: IncomeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            db.incomeDao().deleteIncome(income)  // Lösche den Eintrag aus der DB

            withContext(Dispatchers.Main) {
                Toast.makeText(this@IncomeEntriesActivity, "Income deleted", Toast.LENGTH_SHORT).show()
                loadDatabase()  // RecyclerView aktualisieren
            }
        }
    }
}