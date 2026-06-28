package com.example.fintrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val limitAmount: Double,
    val monthYear: String // Formato "yyyy-MM"
)
