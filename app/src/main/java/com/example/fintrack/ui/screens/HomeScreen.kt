package com.example.fintrack.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintrack.FintrackApplication
import com.example.fintrack.R
import com.example.fintrack.model.MonthlySummary
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import com.example.fintrack.ui.components.AddTransactionBottomSheet
import com.example.fintrack.ui.components.SummaryCard
import com.example.fintrack.ui.components.TransactionItem
import com.example.fintrack.ui.screens.viewmodel.HomeUiState
import com.example.fintrack.ui.screens.viewmodel.HomeViewModel
import com.example.fintrack.ui.screens.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val app = LocalContext.current.applicationContext as FintrackApplication
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(app.repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onAddTransaction = { viewModel.addTransaction(it) },
        onDeleteTransaction = { viewModel.deleteTransaction(it) }, 
        onEditTransaction = { viewModel.editTransaction(it) },
        onDateSelected = { viewModel.updateMonthYear(it) },
        onSwipeNext = { viewModel.nextMonth() },
        onSwipePrevious = { viewModel.previousMonth() }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAddTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDateSelected: (YearMonth) -> Unit,
    onSwipeNext: () -> Unit,
    onSwipePrevious: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val offsetY = remember { Animatable(0f) }
    val dragThreshold = 250f

    suspend fun animateAndSwipe(goingForward: Boolean) {
        offsetY.animateTo(
            targetValue = if (goingForward) 80f else -80f,
            animationSpec = tween(durationMillis = 180, easing = EaseOut)
        )
        if (goingForward) onSwipeNext() else onSwipePrevious()
        offsetY.snapTo(if (goingForward) -60f else 60f)
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        var dragAccum = 0f

                        do {
                            val event = awaitPointerEvent(pass = PointerEventPass.Final)
                            val drag = event.changes
                                .map { it.position.y - it.previousPosition.y }
                                .average()
                                .toFloat()

                            val atTop = !listState.canScrollBackward
                            val atBottom = !listState.canScrollForward

                            when {
                                atTop && drag > 0f -> {
                                    dragAccum += drag
                                    if (dragAccum >= dragThreshold && !offsetY.isRunning) {
                                        dragAccum = 0f
                                        scope.launch { animateAndSwipe(goingForward = true) }
                                    }
                                }

                                atBottom && drag < 0f -> {
                                    dragAccum += drag
                                    if (dragAccum <= -dragThreshold && !offsetY.isRunning) {
                                        dragAccum = 0f
                                        scope.launch { animateAndSwipe(goingForward = false) }
                                    }
                                }

                                else -> dragAccum = 0f
                            }

                        } while (event.changes.any { it.pressed })

                        dragAccum = 0f
                    }
                },
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            item {
                SummaryCard(
                    summary = uiState.summary,
                    selectedDate = uiState.selectedDate,
                    onDateSelected = onDateSelected
                )
            }
            item {
                Text(
                    text = stringResource(R.string.transactions_list_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (uiState.visibleTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.empty_transactions),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(
                    items = uiState.visibleTransactions,
                    key = { _, item -> item.id }
                ) { index, transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onDelete = onDeleteTransaction,
                        onEdit = {
                            transactionToEdit = it
                            showBottomSheet = true
                        }
                    )
                    if (index < uiState.visibleTransactions.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                transactionToEdit = null
                showBottomSheet = true
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Adicionar transação")
        }

        if (showBottomSheet) {
            AddTransactionBottomSheet(
                transaction = transactionToEdit,
                onDismiss = {
                    showBottomSheet = false
                    transactionToEdit = null
                },
                onConfirm = { transaction ->
                    if (transactionToEdit != null) onEditTransaction(transaction)
                    else onAddTransaction(transaction)
                    showBottomSheet = false
                    transactionToEdit = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockState = HomeUiState(
        selectedDate = YearMonth.of(2025, 5),
        summary = MonthlySummary(
            month = "Maio 2025",
            expenses = 3000.0,
            income = 8000.0
        ),
        visibleTransactions = listOf(
            Transaction(
                1,
                "Taxi",
                45.00,
                TransactionType.EXPENSE,
                TransactionCategory.TRANSPORT,
                LocalDate.of(2025, 5, 10)
            ),
            Transaction(
                2,
                "Salary",
                8000.00,
                TransactionType.INCOME,
                TransactionCategory.SALARY,
                LocalDate.of(2025, 5, 5)
            ),
            Transaction(
                3,
                "Internet",
                120.00,
                TransactionType.EXPENSE,
                TransactionCategory.INTERNET,
                LocalDate.of(2025, 5, 15)
            ),
            Transaction(
                4,
                "Food",
                85.50,
                TransactionType.EXPENSE,
                TransactionCategory.FOOD,
                LocalDate.of(2025, 5, 20)
            )
        )
    )

    HomeContent(
        uiState = mockState,
        onAddTransaction = {},
        onDeleteTransaction = {},
        onEditTransaction = {},
        onDateSelected = {},
        onSwipeNext = {},
        onSwipePrevious = {}
    )
}