package com.example.fintrack.model

data class MonthlySummary(
    val month    : String,
    val expenses : Double,
    val income   : Double
) {
    val balance: Double get() = income - expenses
}
