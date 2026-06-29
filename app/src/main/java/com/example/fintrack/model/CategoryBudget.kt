package com.example.fintrack.model

data class CategoryBudget(
    val id: Int = 0,
    val category: TransactionCategory,
    val limitAmount: Double,
    val spentAmount: Double
) {
    val progress: Float
        get() = if (limitAmount > 0.0) (spentAmount / limitAmount).toFloat() else 0f
    
    // CORREÇÃO: Só considera ultrapassado se houver um limite definido (> 0)
    val isOverBudget: Boolean
        get() = limitAmount > 0.0 && spentAmount > limitAmount
}
