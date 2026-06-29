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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintrack.FintrackApplication
import com.example.fintrack.model.CategoryBudget
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.ui.components.SummaryCard
import com.example.fintrack.ui.components.getCategoryIcon
import com.example.fintrack.ui.components.getCategoryStyle
import com.example.fintrack.ui.screens.viewmodel.BudgetViewModel
import com.example.fintrack.ui.theme.FintrackTheme

@Composable
fun BudgetScreen() {
    val app = LocalContext.current.applicationContext as FintrackApplication
    val viewModel: BudgetViewModel = viewModel(
        factory = BudgetViewModel.Factory(app.budgetRepository, app.repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf<TransactionCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Orçamento",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
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
                SummaryCard(
                    summary = uiState.summary,
                    selectedDate = uiState.selectedDate,
                    onDateSelected = { viewModel.updateMonthYear(it) }
                )
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

            items(uiState.budgets) { budget ->
                BudgetCard(
                    budget = budget,
                    onEditClick = { showEditDialog = budget.category }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }

    if (showEditDialog != null) {
        val category = showEditDialog!!
        val currentLimit = uiState.budgets.find { it.category == category }?.limitAmount ?: 0.0
        
        EditBudgetDialog(
            category = category,
            initialValue = currentLimit,
            onDismiss = { showEditDialog = null },
            onConfirm = { newLimit ->
                viewModel.setBudgetLimit(category, newLimit)
                showEditDialog = null
            }
        )
    }
}

@Composable
fun BudgetCard(
    budget: CategoryBudget,
    onEditClick: () -> Unit
) {
    val style = getCategoryStyle(budget.category)
    val hasLimit = budget.limitAmount > 0.0
    val percent = if (hasLimit) (budget.progress * 100).toInt().coerceIn(0, 999) else 0

    val amountColor = when {
        budget.isOverBudget -> MaterialTheme.colorScheme.error
        budget.progress >= 1f && hasLimit -> Color(0xFFBA7517)
        else -> style.iconTint
    }

    val progressColor = when {
        budget.isOverBudget -> MaterialTheme.colorScheme.error
        budget.progress >= 1f && hasLimit -> Color(0xFFEF9F27)
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
                    Icon(
                        imageVector = getCategoryIcon(budget.category),
                        contentDescription = null,
                        tint = style.iconTint,
                        modifier = Modifier.size(20.dp),
                    )
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
                            budget.progress >= 1f && hasLimit -> "Limite atingido"
                            hasLimit -> "$percent% utilizado"
                            else -> "Sem limite definido"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            budget.isOverBudget -> MaterialTheme.colorScheme.error
                            budget.progress >= 1f && hasLimit -> Color(0xFFBA7517)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "R$ %.0f".format(budget.spentAmount),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = amountColor,
                        )
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier.size(24.dp).padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Editar Limite",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (hasLimit) {
                        Text(
                            text = "de R$ %.0f".format(budget.limitAmount),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (hasLimit) {
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
            }

            if (budget.isOverBudget) {
                Spacer(modifier = Modifier.height(8.dp))
                AlertBadge(
                    text = "Você ultrapassou o limite",
                    textColor = Color(0xFFA32D2D),
                    bgColor = Color(0xFFFCEBEB),
                )
            } else if (budget.progress >= 1f && hasLimit) {
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
fun EditBudgetDialog(
    category: TransactionCategory,
    initialValue: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var value by remember { mutableStateOf(if (initialValue > 0) initialValue.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Definir Limite: ${category.label}") },
        text = {
            Column {
                Text("Quanto você planeja gastar nesta categoria este mês?", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Limite (R$)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { 
                val newLimit = value.toDoubleOrNull() ?: 0.0
                onConfirm(newLimit)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetScreenPreview() {
    FintrackTheme {
        BudgetScreen()
    }
}
