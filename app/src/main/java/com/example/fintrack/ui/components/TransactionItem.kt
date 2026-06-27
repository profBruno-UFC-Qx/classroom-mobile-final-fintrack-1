package com.example.fintrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import com.example.fintrack.ui.theme.FintrackTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class DragValue { Settled, Open }

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDelete: (Transaction) -> Unit,
    onEdit: (Transaction) -> Unit
) {
    val isExpense = transaction.type == TransactionType.EXPENSE
    val style = getCategoryStyle(transaction.category)

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val actionsWidth = 160.dp
    val actionsWidthPx = with(density) { actionsWidth.toPx() }

    val dragState = remember(actionsWidthPx) {
        AnchoredDraggableState(
            initialValue = DragValue.Settled,
            anchors = DraggableAnchors {
                DragValue.Settled at 0f
                DragValue.Open at -actionsWidthPx
            }
        )
    }

    val progress = remember(dragState) {
        derivedStateOf {
            val offset = dragState.offset
            if (offset.isNaN() || actionsWidthPx == 0f) 0f
            else (abs(offset) / actionsWidthPx).coerceIn(0f, 1f)
        }
    }

    fun closeThen(action: () -> Unit) {
        scope.launch {
            dragState.animateTo(DragValue.Settled)
            action()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(actionsWidth),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .graphicsLayer {
                            alpha = progress.value
                            scaleX = 0.6f + (progress.value * 0.4f)
                            scaleY = 0.6f + (progress.value * 0.4f)
                        }
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { closeThen { onEdit(transaction) } },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .graphicsLayer {
                            alpha = progress.value
                            scaleX = 0.6f + (progress.value * 0.4f)
                            scaleY = 0.6f + (progress.value * 0.4f)
                        }
                        .background(MaterialTheme.colorScheme.error)
                        .clickable { closeThen { onDelete(transaction) } },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Deletar",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(x = dragState.requireOffset().roundToInt(), y = 0) }
                .anchoredDraggable(state = dragState, orientation = Orientation.Horizontal)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(enabled = dragState.currentValue == DragValue.Settled) {
                    onEdit(transaction)
                }
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(style.iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(transaction.category),
                            contentDescription = transaction.category.label,
                            modifier = Modifier.size(22.dp),
                            tint = style.iconTint
                        )
                    }
                },
                headlineContent = {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                trailingContent = {
                    Text(
                        text = if (isExpense) "- R$ %.2f".format(transaction.amount)
                        else "+ R$ %.2f".format(transaction.amount),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isExpense) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    FintrackTheme {
        TransactionItem(
            transaction = Transaction(
                id = 1,
                title = "Almoço",
                amount = 35.50,
                type = TransactionType.EXPENSE,
                category = TransactionCategory.FOOD,
                date = LocalDate.now()
            ),
            onDelete = {},
            onEdit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemIncomePreview() {
    FintrackTheme {
        TransactionItem(
            transaction = Transaction(
                id = 2,
                title = "Salário",
                amount = 5000.00,
                type = TransactionType.INCOME,
                category = TransactionCategory.SALARY,
                date = LocalDate.now()
            ),
            onDelete = {},
            onEdit = {}
        )
    }
}
