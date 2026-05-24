package com.example.fintrack.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType

@Composable
fun TransactionItem(transaction: Transaction) {
    val isExpense = transaction.type == TransactionType.EXPENSE

    ListItem(
        leadingContent = {
            Surface(
                shape = CircleShape,
            ) {
                Icon(
                    imageVector        = categoryIcon(transaction.category),
                    contentDescription = transaction.category.label,
                    modifier           = Modifier.padding(8.dp).size(20.dp)
                )
            }
        },
        headlineContent = {
            Text(transaction.title)
        },
        trailingContent = {
            Text(
                text       = if (isExpense) "- R$ %.2f".format(transaction.amount)
                else "+ R$ %.2f".format(transaction.amount),
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    )
}

private fun categoryIcon(category: TransactionCategory): ImageVector = when (category) {
    TransactionCategory.FOOD      -> Icons.Outlined.Restaurant
    TransactionCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
    TransactionCategory.SALARY    -> Icons.Outlined.AccountBalance
    TransactionCategory.INTERNET  -> Icons.Outlined.Wifi
    TransactionCategory.HEALTH    -> Icons.Outlined.HealthAndSafety
    TransactionCategory.SHOPPING  -> Icons.Outlined.ShoppingBag
    TransactionCategory.OTHER     -> Icons.Outlined.Category
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