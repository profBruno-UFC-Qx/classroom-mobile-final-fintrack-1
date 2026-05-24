package com.example.fintrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomBar(
    currentScreen : String,
    onHomeClick   : () -> Unit,
    onChartsClick : () -> Unit,
    onBudgetClick : () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick  = onHomeClick,
            icon     = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
            label    = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentScreen == "charts",
            onClick  = onChartsClick,
            icon     = { Icon(Icons.Outlined.BarChart, contentDescription = "Charts") },
            label    = { Text("Charts") }
        )
        NavigationBarItem(
            selected = currentScreen == "budget",
            onClick  = onBudgetClick,
            icon     = { Icon(Icons.Outlined.Savings, contentDescription = "Budget") },
            label    = { Text("Budget") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar(
        currentScreen = "home",
        onHomeClick   = {},
        onChartsClick = {},
        onBudgetClick = {}
    )
}