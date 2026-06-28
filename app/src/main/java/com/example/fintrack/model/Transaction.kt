package com.example.fintrack.model

enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory(val label: String, val type: TransactionType) {
    // INCOME
    SALARY("Salário", TransactionType.INCOME),
    INVESTMENT("Investimento", TransactionType.INCOME),
    GIFT("Presente", TransactionType.INCOME),
    OTHER_INCOME("Outros", TransactionType.INCOME),
    
    // EXPENSE
    FOOD("Alimentação", TransactionType.EXPENSE),
    TRANSPORT("Transporte", TransactionType.EXPENSE),
    HEALTH("Saúde", TransactionType.EXPENSE),
    INTERNET("Internet", TransactionType.EXPENSE),
    SHOPPING("Compras", TransactionType.EXPENSE),
    OTHER("Outros", TransactionType.EXPENSE),
}

data class ReceiptItem(
    val name: String,
    val value: Double
)

data class Transaction(
    val id          : Int,
    val title       : String,
    val amount      : Double,
    val type        : TransactionType,
    val category    : TransactionCategory,
    val date        : java.time.LocalDate,
    val description : String? = null,
    val items       : List<ReceiptItem>? = null
)
