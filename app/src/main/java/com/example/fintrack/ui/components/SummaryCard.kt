package com.example.fintrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.R
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.ui.components.charts.MonthYearSelector
import java.time.YearMonth

@Composable
fun SummaryCard(
    summary: MonthlySummary,
    selectedDate: YearMonth,
    onDateSelected: (YearMonth) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceContainerLow,
        )
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MonthYearSelector(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryValue(label = stringResource(R.string.summary_expenses), amount = summary.expenses, isExpense = true)
                VerticalDivider(modifier = Modifier.height(36.dp))
                SummaryValue(label = stringResource(R.string.summary_income),   amount = summary.income,   isExpense = false)
                VerticalDivider(modifier = Modifier.height(36.dp))
                SummaryValue(label = stringResource(R.string.summary_balance),  amount = summary.balance,  isExpense = false)
            }
        }
    }
}

@Composable
private fun SummaryValue(label: String, amount: Double, isExpense: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "R$ %.2f".format(amount),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryCardPreview() {
    SummaryCard(
        summary = MonthlySummary(
            month    = "May",
            expenses = 3000.0,
            income   = 8000.0
        ),
        selectedDate = YearMonth.of(2025, 5),
        onDateSelected = {}
    )
}