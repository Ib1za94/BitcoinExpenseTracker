package com.example.bitcoinexpensetracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var isLoading = false

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun addTransaction(amount: Double, category: String) {
        viewModelScope.launch {
            val transaction = TransactionEntity(amount = amount, category = category, timestamp = System.currentTimeMillis())
            repository.insertTransaction(transaction)
        }
    }

    fun loadMoreTransactions() {
        if (isLoading) return
        isLoading = true
        Log.d("TransactionViewModel", "Loading more transactions...")

        viewModelScope.launch {
            val newTransactions = repository.getTransactionsPaginated(pageSize, currentPage * pageSize)
            if (newTransactions.isNotEmpty()) {
                _transactions.value = _transactions.value + newTransactions
                currentPage++
                Log.d("TransactionViewModel", "Loaded ${newTransactions.size} new transactions.")
            } else {
                Log.d("TransactionViewModel", "No more transactions to load.")
            }
            isLoading = false
        }
    }
}