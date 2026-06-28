package com.example.fintrack.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fintrack.data.repository.BudgetRepository
import com.example.fintrack.data.repository.TransactionRepository
import com.example.fintrack.model.CategoryBudget
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

data class BudgetUiState(
    val selectedDate: YearMonth = YearMonth.now(),
    val summary: MonthlySummary = MonthlySummary("", 0.0, 0.0),
    val budgets: List<CategoryBudget> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetViewModel(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<BudgetUiState> = _selectedDate.flatMapLatest { date ->
        combine(
            budgetRepository.getBudgetsByMonth(date),
            transactionRepository.getAll()
        ) { budgetEntities, transactions ->
            
            val filteredTransactions = transactions.filter { 
                YearMonth.from(it.date) == date 
            }

            val budgets = TransactionCategory.entries.filter { it.type == TransactionType.EXPENSE }.map { category ->
                val entity = budgetEntities.find { it.category == category.name }
                val limit = entity?.limitAmount ?: 0.0
                val spent = filteredTransactions.filter { it.category == category }.sumOf { it.amount }
                CategoryBudget(
                    id = entity?.id ?: 0,
                    category = category, 
                    limitAmount = limit, 
                    spentAmount = spent
                )
            }

            BudgetUiState(
                selectedDate = date,
                summary = buildSummary(filteredTransactions, date.toString()),
                budgets = budgets
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetUiState()
    )

    fun updateMonthYear(newDate: YearMonth) {
        _selectedDate.value = newDate
    }

    fun setBudgetLimit(category: TransactionCategory, limit: Double) {
        viewModelScope.launch {
            val currentBudget = uiState.value.budgets.find { it.category == category }
            budgetRepository.updateBudget(
                id = currentBudget?.id ?: 0,
                category = category.name, 
                limit = limit, 
                date = _selectedDate.value
            )
        }
    }

    private fun buildSummary(transactions: List<com.example.fintrack.model.Transaction>, month: String): MonthlySummary {
        return MonthlySummary(
            month    = month,
            expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount },
            income   = transactions.filter { it.type == TransactionType.INCOME  }.sumOf { it.amount }
        )
    }

    class Factory(
        private val budgetRepository: BudgetRepository,
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BudgetViewModel(budgetRepository, transactionRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
