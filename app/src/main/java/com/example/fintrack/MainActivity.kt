package com.example.fintrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fintrack.ui.components.BottomBar
import com.example.fintrack.ui.components.SelectedScreenState
import com.example.fintrack.ui.components.TopBar
import com.example.fintrack.ui.screens.BudgetScreen
import com.example.fintrack.ui.screens.ChartsScreen
import com.example.fintrack.ui.screens.HomeScreen
import com.example.fintrack.ui.theme.FintrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FintrackTheme {
                DeviceScreen()
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DeviceScreen() {
    var selectedScreen by remember { mutableStateOf(SelectedScreenState.CHARTS.value) }
    Scaffold(
        topBar = {
                TopBar(
                    {},
                    {},
                )
             },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                currentScreen = selectedScreen,
                onHomeClick = { selectedScreen = SelectedScreenState.HOME.value },
                onChartsClick = { selectedScreen = SelectedScreenState.CHARTS.value },
                onBudgetClick = { selectedScreen = SelectedScreenState.BUDGET.value },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when(selectedScreen) {
                SelectedScreenState.HOME.value -> HomeScreen()
                SelectedScreenState.BUDGET.value -> BudgetScreen()
                else -> ChartsScreen()
            }
        }
    }
}