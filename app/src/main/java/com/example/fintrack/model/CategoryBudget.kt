package com.example.fintrack.model

import com.example.fintrack.model.TransactionCategory

data class CategoryBudget(
    val id: Int = 0, // Adicionado ID para permitir atualização no banco
    val category: TransactionCategory,
    val limitAmount: Double,
    val spentAmount: Double
) {
    val progress: Float
        get() = if (limitAmount > 0) (spentAmount / limitAmount).toFloat() else 0f
    
    val isOverBudget: Boolean
        get() = spentAmount > limitAmount
}
