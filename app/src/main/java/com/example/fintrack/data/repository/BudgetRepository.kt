package com.example.fintrack.data.repository

import com.example.fintrack.data.local.BudgetDao
import com.example.fintrack.data.local.BudgetEntity
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

class BudgetRepository(private val budgetDao: BudgetDao) {

    fun getBudgetsByMonth(date: YearMonth): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsByMonth(date.toString())
    }

    suspend fun updateBudget(category: String, limit: Double, date: YearMonth) {
        budgetDao.insertBudget(
            BudgetEntity(
                category = category,
                limitAmount = limit,
                monthYear = date.toString()
            )
        )
    }

    suspend fun deleteBudget(id: Int) {
        budgetDao.deleteBudget(id)
    }
}
