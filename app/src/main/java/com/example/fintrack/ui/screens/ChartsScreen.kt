package com.example.fintrack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.ui.components.charts.CardLineChart
import com.example.fintrack.ui.components.charts.CardPieChartAndCategories
import com.example.fintrack.ui.components.charts.FintrackData
import com.example.fintrack.ui.components.charts.MonthEntry

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChartsScreen(modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    val mockLineChart = listOf(
        MonthEntry("Jan", 1240.23),
        MonthEntry("Fev", 1343.54),
        MonthEntry("Mar", 1432.12),
        MonthEntry("Abr", 1234.21),
    )

    val mockPieChart = listOf(
        FintrackData("Aluguel", 1200.0, Color(0xFF3F00FF)),
        FintrackData("Supermercado", 600.0, Color(0xFF9D27B0)),
        FintrackData("Restaurante", 250.0, Color(0xFFE91E63)),
        FintrackData("Energia", 180.0, Color(0xFFFF9800)),
        FintrackData("Água", 70.0, Color(0xFF00BCD4))
    )

    Column(
        modifier
            .verticalScroll(scrollState),
    ) {
        CardLineChart(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            entries = mockLineChart,
        )

        CardPieChartAndCategories(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 8.dp),
            entries = mockPieChart,
            )
    }
}