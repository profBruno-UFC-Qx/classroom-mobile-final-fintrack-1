package com.example.fintrack.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import com.example.fintrack.ui.components.AddTransactionBottomSheet
import com.example.fintrack.ui.components.BottomBar
import com.example.fintrack.ui.components.SummaryCard
import com.example.fintrack.ui.components.TransactionItem
import com.example.fintrack.ui.screens.viewmodel.HomeUiState
import com.example.fintrack.ui.screens.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState          = uiState,
        onAddTransaction = { viewModel.addTransaction(it) }
    )
}

@Composable
private fun HomeContent(
    uiState          : HomeUiState,
    onAddTransaction : (Transaction) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SummaryCard(summary = uiState.summary)
            }
            item {
                Text(
                    text       = "Transactions",
                    style      = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(16.dp)
                )
            }
            items(items = uiState.transactions, key = { it.id }) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }

        if (showBottomSheet) {
            AddTransactionBottomSheet(
                onDismiss = { showBottomSheet = true },
                onConfirm = { transaction ->
                    onAddTransaction(transaction)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockState = HomeUiState(
        summary = MonthlySummary(
            month    = "May",
            expenses = 3000.0,
            income   = 8000.0
        ),
        transactions = listOf(
            Transaction(1, "Taxi",     45.00,   TransactionType.EXPENSE, TransactionCategory.TRANSPORT),
            Transaction(2, "Salary",   8000.00, TransactionType.INCOME,  TransactionCategory.SALARY),
            Transaction(3, "Internet", 120.00,  TransactionType.EXPENSE, TransactionCategory.INTERNET),
            Transaction(4, "Food",     85.50,   TransactionType.EXPENSE, TransactionCategory.FOOD)
        )
    )

    HomeContent(uiState = mockState, onAddTransaction = {})
}