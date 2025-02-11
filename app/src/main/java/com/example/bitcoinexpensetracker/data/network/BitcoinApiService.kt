package com.example.bitcoinexpensetracker.data.network

import com.example.bitcoinexpensetracker.data.model.BitcoinPriceResponse
import retrofit2.http.GET

interface BitcoinApiService {
    @GET("v1/bpi/currentprice.json")
    suspend fun getBitcoinPrice(): BitcoinPriceResponse
}