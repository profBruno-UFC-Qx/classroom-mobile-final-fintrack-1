package com.example.fintrack.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val summary      : MonthlySummary,
    val transactions : List<Transaction>
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState = _uiState.asStateFlow()

    private fun initialState(): HomeUiState {
        val transactions = listOf(
            Transaction(1, "Taxi",     45.00,   TransactionType.EXPENSE, TransactionCategory.TRANSPORT),
            Transaction(2, "Salary",   8000.00, TransactionType.INCOME,  TransactionCategory.SALARY),
            Transaction(3, "Internet", 120.00,  TransactionType.EXPENSE, TransactionCategory.INTERNET),
            Transaction(4, "Food",     85.50,   TransactionType.EXPENSE, TransactionCategory.FOOD),
            Transaction(5, "Food",     62.00,   TransactionType.EXPENSE, TransactionCategory.FOOD),
            Transaction(6, "Food",     38.90,   TransactionType.EXPENSE, TransactionCategory.FOOD)
        )

        val summary = MonthlySummary(
            month    = "May",
            expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount },
            income   = transactions.filter { it.type == TransactionType.INCOME  }.sumOf { it.amount }
        )

        return HomeUiState(summary = summary, transactions = transactions)
    }
}