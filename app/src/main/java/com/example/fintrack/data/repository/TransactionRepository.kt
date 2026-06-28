package com.example.fintrack.data.repository

import com.example.fintrack.data.local.TransactionDao
import com.example.fintrack.data.local.TransactionEntity
import com.example.fintrack.model.Transaction
import com.example.fintrack.model.TransactionCategory
import com.example.fintrack.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAll(): Flow<List<Transaction>> {
        return transactionDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction.toEntity())
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }

    private fun TransactionEntity.toDomain(): Transaction = Transaction(
        id = id,
        title = title,
        amount = amount,
        type = TransactionType.valueOf(type),
        category = TransactionCategory.valueOf(category),
        date = LocalDate.parse(date)
    )

    private fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
        id = if (id > 10000000) 0 else id, // Se for ID temporário, deixa o Room gerar
        title = title,
        amount = amount,
        type = type.name,
        category = category.name,
        date = date.toString()
    )
}
