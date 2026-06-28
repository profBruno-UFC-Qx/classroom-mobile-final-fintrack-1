package com.example.fintrack

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintrack.ui.components.BottomBar
import com.example.fintrack.ui.components.NewsDrawerContent
import com.example.fintrack.ui.components.SelectedScreenState
import com.example.fintrack.ui.components.TopBar
import com.example.fintrack.ui.screens.BudgetScreen
import com.example.fintrack.ui.screens.ChartsScreen
import com.example.fintrack.ui.screens.HomeScreen
import com.example.fintrack.ui.screens.viewmodel.NewsViewModel
import com.example.fintrack.ui.theme.FintrackTheme
import com.example.fintrack.worker.NotificationScheduler
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("fintrack_prefs", Context.MODE_PRIVATE) }
    
    var notificationsEnabled by remember { 
        mutableStateOf(sharedPrefs.getBoolean("notifications_enabled", false)) 
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationsEnabled = true
            sharedPrefs.edit { putBoolean("notifications_enabled", true) }
            NotificationScheduler.scheduleReminders(context)
        }
    }

    var selectedScreen by remember { mutableStateOf(SelectedScreenState.CHARTS.value) }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val app = LocalContext.current.applicationContext as FintrackApplication
    val newsViewModel: NewsViewModel = viewModel(
        factory = NewsViewModel.Factory(app.newsRepository)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NewsDrawerContent(viewModel = newsViewModel)
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNotificationsClick = {
                        if (notificationsEnabled) {
                            notificationsEnabled = false
                            sharedPrefs.edit { putBoolean("notifications_enabled", false) }
                            NotificationScheduler.cancelReminders(context)
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                notificationsEnabled = true
                                sharedPrefs.edit { putBoolean("notifications_enabled", true) }
                                NotificationScheduler.scheduleReminders(context)
                            }
                        }
                    },
                    notificationsEnabled = notificationsEnabled
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
                when (selectedScreen) {
                    SelectedScreenState.HOME.value -> HomeScreen()
                    SelectedScreenState.BUDGET.value -> BudgetScreen()
                    else -> ChartsScreen()
                }
            }
        }
    }
}