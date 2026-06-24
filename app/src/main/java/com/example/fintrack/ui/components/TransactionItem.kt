package com.example.fintrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionType
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.ui.getCategoryIcon
import com.example.fintrack.ui.getCategoryStyle

@Composable
fun TransactionItem(transaction: Transaction) {
    val isExpense = transaction.type == TransactionType.EXPENSE
    val style = getCategoryStyle(transaction.category)

    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(style.iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = getCategoryIcon(transaction.category),
                    contentDescription = transaction.category.label,
                    modifier           = Modifier.size(22.dp),
                    tint               = style.iconTint
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
                text       = if (isExpense) "- R$ %.2f".format(transaction.amount)
                else "+ R$ %.2f".format(transaction.amount),
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color      = if (isExpense) Color(0xFFA32D2D) else Color(0xFF0F6E56)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TransactionItemExpensePreview() {
    TransactionItem(
        transaction = Transaction(
            id       = 1,
            title    = "Food",
            amount   = 85.50,
            type     = TransactionType.EXPENSE,
            category = TransactionCategory.FOOD
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TransactionItemIncomePreview() {
    TransactionItem(
        transaction = Transaction(
            id       = 2,
            title    = "Salary",
            amount   = 8000.0,
            type     = TransactionType.INCOME,
            category = TransactionCategory.SALARY
        )
    )
}
