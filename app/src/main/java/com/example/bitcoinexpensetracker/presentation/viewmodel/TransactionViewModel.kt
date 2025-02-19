package com.example.bitcoinexpensetracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.data.repository.BitcoinRepository
import com.example.bitcoinexpensetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository,
    private val bitcoinRepository: BitcoinRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions.asStateFlow()

    private val _balance = MutableStateFlow<Double>(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var isLoading = false

    private val _bitcoinPrice = MutableStateFlow<Int?>(null)
    val bitcoinPrice: StateFlow<Int?> = _bitcoinPrice.asStateFlow()

    private var lastUpdateTime: Long = 0

    init {
        fetchTransactions()
        fetchBitcoinPrice()
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactionList ->
                _transactions.value = transactionList
                calculateBalance()
            }
        }
    }

    private fun calculateBalance() {
        _balance.value = _transactions.value.sumOf { it.amount }
    }

    fun fetchBitcoinPrice() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            if (lastUpdateTime == 0L || (currentTime - lastUpdateTime) >= 60 * 60 * 1000) {
                try {
                    val response = bitcoinRepository.getBitcoinPrice()
                    val price = response.bpi.usd.rate
                    _bitcoinPrice.value = price.toInt()
                    lastUpdateTime = currentTime
                    Log.d("TransactionViewModel", "Bitcoin price updated: $price")
                } catch (e: Exception) {
                    Log.e("TransactionViewModel", "Error fetching Bitcoin price", e)
                }
            } else {
                Log.d("TransactionViewModel", "Skipping update, last update was too recent.")
            }
        }
    }

    fun addTransaction(transaction: TransactionEntity) {
        if (transaction.amount == 0.0) {
            Log.d("TransactionViewModel", "Игнорируем транзакцию с нулевой суммой")
            return
        }

        viewModelScope.launch {
            val currentBalance = repository.getBalance()
            if (currentBalance + transaction.amount >= 0) {
                repository.insertTransaction(transaction)
                fetchTransactions()
            } else {
                Log.d("TransactionViewModel", "Недостаточно средств для транзакции")
            }
        }
    }

    fun loadMoreTransactions() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val newTransactions =
                repository.getTransactionsPaginated(pageSize, currentPage * pageSize)
            if (newTransactions.isNotEmpty()) {
                _transactions.value = _transactions.value + newTransactions
                currentPage++
            }
            isLoading = false
        }
    }
}