package pixelhub.financeplaner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers

class FinanceViewModel(application: Application) : AndroidViewModel(application) {

    // Create repository instance
    private val repository = IncomeRepository(application)

    // LiveData for distinct income types
    private val _incomeTypes = MutableLiveData<List<String>>()
    val incomeTypes: LiveData<List<String>> get() = _incomeTypes

    // Function to add income
    fun addIncome(income: IncomeEntry) {
        viewModelScope.launch {
            repository.addIncome(income)
        }
    }

    // Function to load distinct income types
    fun loadIncomeTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            val types = repository.getAllIncomeTypes()
            _incomeTypes.postValue(types)
        }
    }
}