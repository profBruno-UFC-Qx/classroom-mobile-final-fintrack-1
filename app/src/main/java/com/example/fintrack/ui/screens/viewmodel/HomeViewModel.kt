package com.example.fintrack.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class HomeUiState(
    val selectedDate : YearMonth = YearMonth.now(),
    val summary      : MonthlySummary = MonthlySummary("", 0.0, 0.0),
    val visibleTransactions : List<Transaction> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _allTransactions = MutableStateFlow(initialTransactions())
    private val _selectedDate = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<HomeUiState> = combine(
        _allTransactions,
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
        _allTransactions.value = listOf(transaction) + _allTransactions.value
    }

    fun deleteTransaction(id: Int) {
        _allTransactions.value = _allTransactions.value.filter { it.id != id }
    }

    fun editTransaction(transaction: Transaction) {
        _allTransactions.value = _allTransactions.value.map {
            if (it.id == transaction.id) transaction else it
        }
    }

    private fun buildSummary(transactions: List<Transaction>, month: String): MonthlySummary {
        return MonthlySummary(
            month    = month,
            expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount },
            income   = transactions.filter { it.type == TransactionType.INCOME  }.sumOf { it.amount }
        )
    }

    private fun initialTransactions(): List<Transaction> {
        return listOf(
            Transaction(1, "Taxi",     45.00,   TransactionType.EXPENSE, TransactionCategory.TRANSPORT, LocalDate.of(2025, 5, 10)),
            Transaction(2, "Salary",   8000.00, TransactionType.INCOME,  TransactionCategory.SALARY,    LocalDate.of(2025, 5, 5)),
            Transaction(3, "Internet", 120.00,  TransactionType.EXPENSE, TransactionCategory.INTERNET,  LocalDate.of(2025, 5, 15)),
            Transaction(4, "Food",     85.50,   TransactionType.EXPENSE, TransactionCategory.FOOD,      LocalDate.of(2025, 6, 2)),
            Transaction(5, "Shopping", 200.00,  TransactionType.EXPENSE, TransactionCategory.SHOPPING,  LocalDate.of(2025, 6, 20)),
            Transaction(6, "Health",   150.00,  TransactionType.EXPENSE, TransactionCategory.HEALTH,    LocalDate.of(2025, 7, 10))
        )
    }
}
