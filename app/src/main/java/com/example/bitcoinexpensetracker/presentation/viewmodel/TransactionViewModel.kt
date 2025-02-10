package com.example.bitcoinexpensetracker.presentation.viewmodel

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
}