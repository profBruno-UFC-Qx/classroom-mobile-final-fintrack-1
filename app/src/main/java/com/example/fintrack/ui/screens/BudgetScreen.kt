package com.example.fintrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fintrack.model.CategoryBudget
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.ui.components.charts.MonthYearSelector
import com.example.fintrack.ui.theme.FintrackTheme
import java.time.YearMonth

// Estilo visual por categoria
private data class CategoryStyle(
    val iconBg: Color,
    val iconTint: Color,
    val progressColor: Color,
)

private fun categoryStyle(category: TransactionCategory): CategoryStyle = when (category) {
    TransactionCategory.FOOD -> CategoryStyle(
        iconBg = Color(0xFFEAF3DE),
        iconTint = Color(0xFF3B6D11),
        progressColor = Color(0xFF639922),
    )
    TransactionCategory.TRANSPORT -> CategoryStyle(
        iconBg = Color(0xFFFCEBEB),
        iconTint = Color(0xFFA32D2D),
        progressColor = Color(0xFFE24B4A),
    )
    TransactionCategory.SHOPPING -> CategoryStyle(
        iconBg = Color(0xFFEEEDFE),
        iconTint = Color(0xFF534AB7),
        progressColor = Color(0xFF7F77DD),
    )
    TransactionCategory.HEALTH -> CategoryStyle(
        iconBg = Color(0xFFE1F5EE),
        iconTint = Color(0xFF0F6E56),
        progressColor = Color(0xFF1D9E75),
    )
    TransactionCategory.INTERNET -> CategoryStyle(
        iconBg = Color(0xFFFAEEDA),
        iconTint = Color(0xFF854F0B),
        progressColor = Color(0xFFEF9F27),
    )
    TransactionCategory.OTHER -> CategoryStyle(
        iconBg = Color(0xFFF1EFE8),
        iconTint = Color(0xFF5F5E5A),
        progressColor = Color(0xFF888780),
    )
    else -> CategoryStyle(
        iconBg = Color(0xFFF1EFE8),
        iconTint = Color(0xFF5F5E5A),
        progressColor = Color(0xFF888780),
    )
}

@Composable
private fun categoryIcon(category: TransactionCategory): ImageVector? = null

@Composable
fun BudgetScreen(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf(YearMonth.now()) }

    // TODO: Integrar com banco de dados real
    val mockBudgets = listOf(
        CategoryBudget(TransactionCategory.FOOD, 800.0, 450.0),
        CategoryBudget(TransactionCategory.TRANSPORT, 300.0, 320.0),
        CategoryBudget(TransactionCategory.SHOPPING, 500.0, 150.0),
        CategoryBudget(TransactionCategory.HEALTH, 200.0, 50.0),
        CategoryBudget(TransactionCategory.INTERNET, 100.0, 100.0),
        CategoryBudget(TransactionCategory.OTHER, 150.0, 20.0),
    )

    val totalLimit = mockBudgets.sumOf { it.limitAmount }
    val totalSpent = mockBudgets.sumOf { it.spentAmount }
    val totalAvailable = totalLimit - totalSpent

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Orçamento",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8E4FF)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "👤", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                MonthYearSelector(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SummaryRow(
                    totalLimit = totalLimit,
                    totalSpent = totalSpent,
                    totalAvailable = totalAvailable,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "CATEGORIAS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }

            items(mockBudgets) { budget ->
                BudgetCard(budget)
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun SummaryRow(
    totalLimit: Double,
    totalSpent: Double,
    totalAvailable: Double,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SummaryCard(
            label = "Limite total",
            value = "R$ %.0f".format(totalLimit),
            valueColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        SummaryCard(
            label = "Gasto",
            value = "R$ %.0f".format(totalSpent),
            valueColor = Color(0xFFBA7517),
            modifier = Modifier.weight(1f),
        )
        SummaryCard(
            label = "Disponível",
            value = "R$ %.0f".format(totalAvailable),
            valueColor = Color(0xFF1D9E75),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = valueColor,
            )
        }
    }
}

@Composable
fun BudgetCard(budget: CategoryBudget) {
    val style = categoryStyle(budget.category)
    val percent = (budget.progress * 100).toInt().coerceIn(0, 999)

    val amountColor = when {
        budget.isOverBudget -> MaterialTheme.colorScheme.error
        budget.progress >= 1f -> Color(0xFFBA7517)
        else -> style.iconTint
    }

    val progressColor = when {
        budget.isOverBudget -> MaterialTheme.colorScheme.error
        budget.progress >= 1f -> Color(0xFFEF9F27)
        else -> style.progressColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp, 14.dp, 16.dp, 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(style.iconBg),
                    contentAlignment = Alignment.Center,
                ) {
                    val icon = categoryIcon(budget.category)
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = style.iconTint,
                            modifier = Modifier.size(20.dp),
                        )
                    } else {
                        Text(text = budget.category.emoji, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.category.label,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = when {
                            budget.isOverBudget -> "Limite ultrapassado"
                            budget.progress >= 1f -> "Limite atingido"
                            else -> "$percent% utilizado"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            budget.isOverBudget -> MaterialTheme.colorScheme.error
                            budget.progress >= 1f -> Color(0xFFBA7517)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "R$ %.0f".format(budget.spentAmount),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = amountColor,
                    )
                    Text(
                        text = "de R$ %.0f".format(budget.limitAmount),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { budget.progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(99.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            if (budget.isOverBudget) {
                Spacer(modifier = Modifier.height(8.dp))
                AlertBadge(
                    text = "Você ultrapassou o limite",
                    textColor = Color(0xFFA32D2D),
                    bgColor = Color(0xFFFCEBEB),
                )
            } else if (budget.progress >= 1f) {
                Spacer(modifier = Modifier.height(8.dp))
                AlertBadge(
                    text = "Limite atingido",
                    textColor = Color(0xFF854F0B),
                    bgColor = Color(0xFFFAEEDA),
                )
            }
        }
    }
}

@Composable
private fun AlertBadge(text: String, textColor: Color, bgColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ── Preview ────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetScreenPreview() {
    FintrackTheme {
        BudgetScreen()
    }
}