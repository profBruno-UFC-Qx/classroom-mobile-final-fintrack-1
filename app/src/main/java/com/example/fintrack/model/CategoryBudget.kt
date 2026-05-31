package com.example.fintrack.model

import com.example.fintrack.model.TransactionCategory

data class CategoryBudget(
    val category: TransactionCategory,
    val limitAmount: Double,
    val spentAmount: Double
) {
    val progress: Float
        get() = if (limitAmount > 0) (spentAmount / limitAmount).toFloat() else 0f
    
    val isOverBudget: Boolean
        get() = spentAmount > limitAmount
}
