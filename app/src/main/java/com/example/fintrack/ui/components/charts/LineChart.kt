package com.example.fintrack.ui.components.charts

import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
fun LineChart(entries: List<MonthEntry>, modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }

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
fun CardLineChart(entries: List<MonthEntry>, modifier: Modifier = Modifier) {
    ChartCard(modifier) {
        ChartColumn {
            ChartTitle(text = stringResource(R.string.line_chart_title))
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