package com.example.fintrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fintrack.R
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.delay
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    transaction : Transaction? = null,
    onDismiss : () -> Unit,
    onConfirm : (Transaction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title            by remember { mutableStateOf(transaction?.title ?: "") }
    var amount           by remember { mutableStateOf(transaction?.amount?.toString() ?: "") }
    var type             by remember { mutableStateOf(transaction?.type ?: TransactionType.EXPENSE) }
    var category         by remember { mutableStateOf(transaction?.category ?: TransactionCategory.OTHER) }
    var date             by remember { mutableStateOf(transaction?.date ?: LocalDate.now()) }
    var categoryExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState
    ) {
        BottomSheetContent(
            title            = title,
            amount           = amount,
            type             = type,
            category         = category,
            categoryExpanded = categoryExpanded,
            onTitleChange    = { title = it },
            onAmountChange   = { amount = it },
            onTypeChange     = {
                type     = it

                // reseta para a primeira categoria disponível do novo tipo
                category = TransactionCategory.entries.first { cat -> cat.type == it }
            },
            onCategoryChange = { category = it },
            onExpandChange   = { categoryExpanded = it },
            onDismiss        = onDismiss,
            onConfirm        = {
                val value = amount.toDoubleOrNull() ?: 0.0
                if (value > 0) {
                    onConfirm(
                        Transaction(
                            id       = transaction?.id ?: System.currentTimeMillis().toInt(),
                            title    = title.ifBlank { category.label },
                            amount   = value,
                            type     = type,
                            category = category,
                            date     = date
                        )
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetContent(
    title            : String,
    amount           : String,
    type             : TransactionType,
    category         : TransactionCategory,
    categoryExpanded : Boolean,
    onTitleChange    : (String) -> Unit,
    onAmountChange   : (String) -> Unit,
    onTypeChange     : (TransactionType) -> Unit,
    onCategoryChange : (TransactionCategory) -> Unit,
    onExpandChange   : (Boolean) -> Unit,
    onDismiss        : () -> Unit,
    onConfirm        : () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(100) 
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val categories = remember(type) {
        TransactionCategory.entries.filter { it.type == type }
    }

    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text       = stringResource(R.string.add_transaction_title),
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // seletor de tipo
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TransactionType.entries.forEach { t ->
                FilterChip(
                    selected = type == t,
                    onClick  = { onTypeChange(t) },
                    label    = { 
                        val label = when(t) {
                            TransactionType.INCOME -> stringResource(R.string.type_income)
                            TransactionType.EXPENSE -> stringResource(R.string.type_expense)
                        }
                        Text(label) 
                    }
                )
            }
        }

        OutlinedTextField(
            value           = amount,
            onValueChange   = { onAmountChange(it) },
            label           = { Text(stringResource(R.string.label_value)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine      = true,
            modifier        = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        OutlinedTextField(
            value         = title,
            onValueChange = { onTitleChange(it) },
            label         = { Text(stringResource(R.string.label_title)) },
            placeholder   = { Text(stringResource(R.string.placeholder_title)) },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth()
        )

        // menu suspenso de opções
        ExposedDropdownMenuBox(
            expanded         = categoryExpanded,
            onExpandedChange = { onExpandChange(it) },
            modifier         = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value         = category.label,
                onValueChange = {},
                readOnly      = true,
                label         = { Text(stringResource(R.string.label_category)) },
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
            )
            ExposedDropdownMenu(
                expanded         = categoryExpanded,
                onDismissRequest = { onExpandChange(false) }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text    = { Text(cat.label) },
                        onClick = {
                            onCategoryChange(cat)
                            onExpandChange(false)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick  = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.btn_cancel))
            }
            Button(
                onClick  = onConfirm,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.btn_confirm))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionBottomSheetPreview() {
    BottomSheetContent(
        title            = "",
        amount           = "",
        type             = TransactionType.EXPENSE,
        category         = TransactionCategory.FOOD,
        categoryExpanded = false,
        onTitleChange    = {},
        onAmountChange   = {},
        onTypeChange     = {},
        onCategoryChange = {},
        onExpandChange   = {},
        onDismiss        = {},
        onConfirm        = {}
    )
}
