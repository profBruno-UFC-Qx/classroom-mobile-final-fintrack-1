package com.example.fintrack.model

enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory(val label: String) {
    FOOD("Food"),
    TRANSPORT("Transport"),
    HEALTH("Healthcare"),
    SALARY("Salary"),
    INTERNET("Internet"),
    SHOPPING("Shopping"),
    OTHER("Other")
}

data class Transaction(
    val id       : Int,
    val title    : String,
    val amount   : Double,
    val type     : TransactionType,
    val category : TransactionCategory
)