package pixelhub.financeplaner

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerEntryTypes: Spinner
    private lateinit var etAmount: EditText
    private lateinit var tvDate: TextView
    private lateinit var btnAdd: ImageButton
    private val incomeTypes = mutableListOf<String>()

    private lateinit var financeViewModel: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        val factory = FinanceViewModelFactory(application)
        financeViewModel = ViewModelProvider(this, factory)[FinanceViewModel::class.java]

        // Observe income types and update the spinner
        financeViewModel.incomeTypes.observe(this) { types ->
            updateEntryTypeSpinner(types)
        }
        // Fetch distinct types from the database
        CoroutineScope(Dispatchers.IO).launch {
            financeViewModel.incomeTypes // Trigger LiveData observation
        }
        // Load entry types from the database
        financeViewModel.loadIncomeTypes()

        spinnerEntryTypes = findViewById(R.id.spinner_entry_types)
        etAmount = findViewById(R.id.et_amount)
        tvDate = findViewById(R.id.tv_date)
        btnAdd = findViewById(R.id.btnAdd)

        // Set up spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incomeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEntryTypes.adapter = adapter

        // Button to add new entry type
        btnAdd.setOnClickListener {
            showCreateEntryDialog()
        }

        // Show Date Picker dialog
        tvDate.setOnClickListener {
            showDatePicker()
        }
    }

    // Show dialog to create new entry type
    private fun showCreateEntryDialog() {
        val inputEditText = EditText(this).apply {
            hint = "Enter new entry type"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val dialogBuilder = AlertDialog.Builder(this).apply {
            setTitle("Create New Entry Type")
            setMessage("Please enter the type of entry you want to create.")
            setView(inputEditText)
            setPositiveButton("OK") { _, _ ->
                val entryType = inputEditText.text.toString().trim()
                if (entryType.isNotEmpty()) {
                    incomeTypes.add(entryType)
                    spinnerEntryTypes.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, incomeTypes)
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }

        dialogBuilder.create().show()
    }

    // Show the DatePicker dialog
    private fun showDatePicker() {
        val todayDate = Calendar.getInstance()
        val year = todayDate.get(Calendar.YEAR)
        val month = todayDate.get(Calendar.MONTH)
        val day = todayDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // Track button logic
    private fun trackCashflow() {
        val entryType = spinnerEntryTypes.selectedItem.toString()
        val amount = etAmount.text.toString().toDouble()
        val date = tvDate.text.toString()

        if (amount != null && date.isNotEmpty()) {
            val income = IncomeEntry(amount = amount, type = entryType, date = date)
            // Insert the data in the database asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                financeViewModel.addIncome(income)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Income tracked!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateEntryTypeSpinner(types: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEntryTypes.adapter = adapter
    }
}

