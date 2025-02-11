package com.example.bitcoinexpensetracker.data.repository

import com.example.bitcoinexpensetracker.data.model.BitcoinPriceResponse
import com.example.bitcoinexpensetracker.data.network.BitcoinApiService
import com.example.bitcoinexpensetracker.data.network.RetrofitInstance

class BitcoinRepository(private val apiService: BitcoinApiService) {
    suspend fun getBitcoinPrice(): BitcoinPriceResponse {
        return apiService.getBitcoinPrice()
    }
}