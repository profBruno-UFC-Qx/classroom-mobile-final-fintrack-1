package com.example.fintrack.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintrack.FintrackApplication
import com.example.fintrack.ui.components.charts.CardLineChart
import com.example.fintrack.ui.components.charts.CardPieChartAndCategories
import com.example.fintrack.ui.components.charts.MonthYearSelector
import com.example.fintrack.ui.screens.viewmodel.ChartsViewModel
import com.example.fintrack.ui.theme.FintrackTheme

@Composable
fun ChartsScreen(modifier: Modifier = Modifier) {
    val app = LocalContext.current.applicationContext as FintrackApplication
    val viewModel: ChartsViewModel = viewModel(
        factory = ChartsViewModel.Factory(app.repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        MonthYearSelector(
            selectedDate = uiState.selectedDate,
            onDateSelected = { viewModel.updateMonthYear(it) },
            modifier = Modifier.padding(8.dp)
        )

        if (uiState.pieChartData.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sem dados para o período selecionado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            CardLineChart(
                modifier = Modifier.padding(horizontal = 8.dp),
                entries = uiState.lineChartData,
            )

            CardPieChartAndCategories(
                modifier = Modifier.padding(8.dp),
                entries = uiState.pieChartData,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChartsScreenPreview() {
    FintrackTheme {
        ChartsScreen()
    }
}
