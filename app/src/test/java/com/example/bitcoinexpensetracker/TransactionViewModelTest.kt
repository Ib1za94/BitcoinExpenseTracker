package com.example.bitcoinexpensetracker

import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.data.repository.BitcoinRepository
import com.example.bitcoinexpensetracker.data.repository.TransactionRepository
import com.example.bitcoinexpensetracker.presentation.viewmodel.TransactionViewModel
import io.mockk.coVerify
import org.junit.Assert.assertNotNull
import org.junit.Test
import io.mockk.mockk


class TransactionViewModelTest {

    private val transactionRepository = mockk<TransactionRepository>(relaxed = true)
    private val bitcoinRepository = mockk<BitcoinRepository>(relaxed = true)

    private val viewModel = TransactionViewModel(transactionRepository, bitcoinRepository)

    @Test
    fun `viewModel should not be null after initialization`() {
        assertNotNull(viewModel)
    }

    @Test
    fun `should add transaction`() {

        val transaction = TransactionEntity(amount = 10.0, category = "groceries")

        viewModel.addTransaction(transaction)

        coVerify { transactionRepository.insertTransaction(transaction) }
    }
}

