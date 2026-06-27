package com.example.fintrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fintrack.model.TransactionCategory

data class CategoryStyle(
    val iconBg: Color,
    val iconTint: Color,
    val progressColor: Color = Color.Gray,
)

fun getCategoryStyle(category: TransactionCategory): CategoryStyle = when (category) {
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
    TransactionCategory.SALARY -> CategoryStyle(
        iconBg = Color(0xFFE3F2FD),
        iconTint = Color(0xFF1976D2),
        progressColor = Color(0xFF2196F3),
    )
}

fun getCategoryIcon(category: TransactionCategory): ImageVector = when (category) {
    TransactionCategory.FOOD      -> Icons.Outlined.Restaurant
    TransactionCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
    TransactionCategory.SALARY    -> Icons.Outlined.AccountBalance
    TransactionCategory.INTERNET  -> Icons.Outlined.Wifi
    TransactionCategory.HEALTH    -> Icons.Outlined.HealthAndSafety
    TransactionCategory.SHOPPING  -> Icons.Outlined.ShoppingBag
    TransactionCategory.OTHER     -> Icons.Outlined.Category
}
