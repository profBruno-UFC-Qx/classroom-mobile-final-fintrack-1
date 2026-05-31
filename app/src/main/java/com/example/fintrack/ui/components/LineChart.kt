package com.example.fintrack.ui.components

import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.R
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart

data class MonthEntry(
    val month: String,
    val value: Double,
)

@Composable
fun LineChart(
    entries: List<MonthEntry>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(entries) { CartesianChartModelProducer() }

    LaunchedEffect(key1 = entries) {
        modelProducer.runTransaction {
            lineSeries { series(entries.map { it.value }) }
        }
    }

    val monthFormatter = remember(entries) {
        CartesianValueFormatter { _, value, _ ->
            entries.getOrNull(value.toInt())?.month ?: ""
        }
    }

    val valueFormatter = remember {
        CartesianValueFormatter { _, value, _ ->
            "R$ ${value.toInt()}"
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = valueFormatter
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = monthFormatter
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        animationSpec = snap()
    )
}

@Composable
fun CardLineChart(
    entries: List<MonthEntry>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.line_chart_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LineChart(entries = entries)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLineChartFintrack() {
    val mock = listOf(
        MonthEntry("Jan", 1240.23),
        MonthEntry("Fev", 1343.54),
        MonthEntry("Mar", 1432.12),
        MonthEntry("Abr", 1234.21),
    )

    Box(modifier = Modifier.padding(16.dp)) {
        CardLineChart(entries = mock)
    }
}