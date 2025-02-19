package com.example.bitcoinexpensetracker.data.repository

import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.data.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun getTransactionsPaginated(limit: Int, offset: Int): List<TransactionEntity> {
        return transactionDao.getTransactionsPaginated(limit, offset)
    }

    suspend fun getBalance(): Double {
        return transactionDao.getBalance() ?: 0.0
    }
}