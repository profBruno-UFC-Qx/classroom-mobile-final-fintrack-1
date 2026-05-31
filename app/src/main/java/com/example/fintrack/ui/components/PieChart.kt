package com.example.fintrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.R
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import com.patrykandpatrick.vico.compose.pie.rememberPieChart

data class FintrackData(
    val category: String,
    val value: Double,
    val color: Color,
)

@Composable
fun PieChart(
    data: List<FintrackData>,
    modifier: Modifier = Modifier,
) {
    val modelProducer = remember { PieChartModelProducer() }

    LaunchedEffect(key1 = data) {
        modelProducer.runTransaction {
            pieSeries {
                series(data.map { it.value })
            }
        }
    }

    val customSliceProvider = remember(data) {
        PieChart.SliceProvider.series(
            data.map { item ->
                PieChart.Slice(
                    fill = Fill(item.color)
                )
            }
        )
    }

    PieChartHost(
        chart = rememberPieChart(
            sliceProvider = customSliceProvider
        ),
        modelProducer = modelProducer,
        modifier = modifier.size(180.dp),
    )
}

@Composable
fun Categories(
    data: List<FintrackData>,
    modifier: Modifier = Modifier
) {
    val totalValue = remember(data) { data.sumOf { it.value }.toFloat().coerceAtLeast(1f) }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        data.forEach { item ->
            val percentage = ((item.value / totalValue) * 100).toInt()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(item.color, CircleShape)
                )
                Text(
                    text = "${item.category} ($percentage%)",
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CardPieChartAndCategories(
    entries: List<FintrackData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.pie_chart_title),
                style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PieChart(entries)
            Spacer(modifier = Modifier.height(32.dp))
            Categories(entries)
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PreviewCardPieChartAndCategories() {
    val mock = listOf(
        FintrackData("Aluguel", 1200.0, Color(0xFF3F00FF)),
        FintrackData("Supermercado", 600.0, Color(0xFF9D27B0)),
        FintrackData("Restaurante", 250.0, Color(0xFFE91E63)),
        FintrackData("Energia", 180.0, Color(0xFFFF9800)),
        FintrackData("Água", 70.0, Color(0xFF00BCD4))
    )

    Box(modifier = Modifier.padding(16.dp)) {
        CardPieChartAndCategories(entries = mock)
    }
}