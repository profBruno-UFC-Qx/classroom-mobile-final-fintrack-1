package com.example.fintrack.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fintrack.data.repository.TransactionRepository
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class HomeUiState(
    val selectedDate : YearMonth = YearMonth.now(),
    val summary      : MonthlySummary = MonthlySummary("", 0.0, 0.0),
    val visibleTransactions : List<Transaction> = emptyList()
)

class HomeViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAll(),
        _selectedDate
    ) { transactions, selectedDate ->
        val filtered = transactions.filter { 
            YearMonth.from(it.date) == selectedDate 
        }
        
        val localePtBr = Locale.forLanguageTag("pt")
        val monthLabel = selectedDate.month.getDisplayName(TextStyle.FULL, localePtBr)
            .replaceFirstChar { it.uppercase() }
        val fullMonthLabel = "$monthLabel ${selectedDate.year}"

        HomeUiState(
            selectedDate = selectedDate,
            summary = buildSummary(filtered, fullMonthLabel),
            visibleTransactions = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun updateMonthYear(newDate: YearMonth) {
        _selectedDate.value = newDate
    }

    fun nextMonth() {
        _selectedDate.value = _selectedDate.value.plusMonths(1)
    }

    fun previousMonth() {
        _selectedDate.value = _selectedDate.value.minusMonths(1)
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    fun editTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    private fun buildSummary(transactions: List<Transaction>, month: String): MonthlySummary {
        return MonthlySummary(
            month    = month,
            expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount },
            income   = transactions.filter { it.type == TransactionType.INCOME  }.sumOf { it.amount }
        )
    }
}

class HomeViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
