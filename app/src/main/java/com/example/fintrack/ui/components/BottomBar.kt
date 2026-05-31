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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fintrack.R

@Composable
fun BottomBar(
    currentScreen : String,
    onHomeClick   : () -> Unit,
    onChartsClick : () -> Unit,
    onBudgetClick : () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == SelectedScreenState.HOME.value,
            onClick  = onHomeClick,
            icon     = { Icon(Icons.Outlined.Home, contentDescription = stringResource(R.string.home)) },
            label    = { Text(stringResource(R.string.home)) }
        )
        NavigationBarItem(
            selected = currentScreen == SelectedScreenState.CHARTS.value,
            onClick  = onChartsClick,
            icon     = { Icon(Icons.Outlined.BarChart, contentDescription = stringResource(R.string.charts)) },
            label    = { Text(stringResource(R.string.charts)) }
        )
        NavigationBarItem(
            selected = currentScreen == SelectedScreenState.BUDGET.value,
            onClick  = onBudgetClick,
            icon     = { Icon(Icons.Outlined.Savings, contentDescription = stringResource(R.string.budget)) },
            label    = { Text(stringResource(R.string.budget)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar(
        currentScreen = SelectedScreenState.HOME.value,
        onHomeClick   = {},
        onChartsClick = {},
        onBudgetClick = {}
    )
}

enum class SelectedScreenState(val value: String) {
    HOME("home_screen"),
    CHARTS("charts_screen"),
    BUDGET("budget_screen")
}