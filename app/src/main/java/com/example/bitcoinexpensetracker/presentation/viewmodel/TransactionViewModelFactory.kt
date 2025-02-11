package com.example.bitcoinexpensetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bitcoinexpensetracker.data.repository.BitcoinRepository
import com.example.bitcoinexpensetracker.data.repository.TransactionRepository

class TransactionViewModelFactory(
    private val repository: TransactionRepository,
    private val bitcoinRepository: BitcoinRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository, bitcoinRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}