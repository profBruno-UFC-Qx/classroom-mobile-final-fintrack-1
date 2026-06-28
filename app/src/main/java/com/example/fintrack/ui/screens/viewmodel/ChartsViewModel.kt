package com.example.fintrack.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fintrack.data.repository.TransactionRepository
import com.example.fintrack.model.TransactionType
import com.example.fintrack.ui.components.charts.FintrackData
import com.example.fintrack.ui.components.charts.MonthEntry
import com.example.fintrack.ui.getCategoryStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class ChartsUiState(
    val selectedDate: YearMonth = YearMonth.now(),
    val pieChartData: List<FintrackData> = emptyList(),
    val lineChartData: List<MonthEntry> = emptyList()
)

class ChartsViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<ChartsUiState> = combine(
        repository.getAll(),
        _selectedDate
    ) { transactions, selectedDate ->
        
        val filteredForMonth = transactions.filter { 
            it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == selectedDate 
        }

        val pieData = filteredForMonth
            .groupBy { it.category }
            .map { (category, list) ->
                FintrackData(
                    category = category.label,
                    value = list.sumOf { it.amount },
                    color = getCategoryStyle(category).iconTint
                )
            }
            .sortedByDescending { it.value }

        val lineData = (0..5).reversed().map { monthOffset ->
            val targetMonth = selectedDate.minusMonths(monthOffset.toLong())
            val monthTransactions = transactions.filter { 
                it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == targetMonth 
            }
            
            val localePtBr = Locale.forLanguageTag("pt")
            val monthLabel = targetMonth.month.getDisplayName(TextStyle.SHORT, localePtBr)
                .replaceFirstChar { it.uppercase() }

            MonthEntry(
                month = monthLabel,
                value = monthTransactions.sumOf { it.amount }
            )
        }

        ChartsUiState(
            selectedDate = selectedDate,
            pieChartData = pieData,
            lineChartData = lineData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChartsUiState()
    )

    fun updateMonthYear(newDate: YearMonth) {
        _selectedDate.value = newDate
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChartsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChartsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
