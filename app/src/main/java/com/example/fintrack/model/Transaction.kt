package com.example.fintrack.model

enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory(val label: String, val type: TransactionType) {
    SALARY("Salary",TransactionType.INCOME),
    FOOD("Food", TransactionType.EXPENSE),
    TRANSPORT("Transport",TransactionType.EXPENSE),
    HEALTH("Healthcare",TransactionType.EXPENSE),
    INTERNET("Internet",TransactionType.EXPENSE),
    SHOPPING("Shopping",TransactionType.EXPENSE),
    OTHER("Other",TransactionType.EXPENSE),
}

data class Transaction(
    val id       : Int,
    val title    : String,
    val amount   : Double,
    val type     : TransactionType,
    val category : TransactionCategory
)