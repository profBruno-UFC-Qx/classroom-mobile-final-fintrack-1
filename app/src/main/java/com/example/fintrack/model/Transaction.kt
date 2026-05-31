package com.example.fintrack.model

enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory(val label: String, val emoji: String, val type: TransactionType) {
    SALARY("Salário", "💰", TransactionType.INCOME),
    FOOD("Alimentação", "🍽️", TransactionType.EXPENSE),
    TRANSPORT("Transporte", "🚗", TransactionType.EXPENSE),
    HEALTH("Saúde", "❤️", TransactionType.EXPENSE),
    INTERNET("Internet", "📶", TransactionType.EXPENSE),
    SHOPPING("Compras", "🛍️", TransactionType.EXPENSE),
    OTHER("Outros", "•••", TransactionType.EXPENSE),
}

data class Transaction(
    val id       : Int,
    val title    : String,
    val amount   : Double,
    val type     : TransactionType,
    val category : TransactionCategory
)
