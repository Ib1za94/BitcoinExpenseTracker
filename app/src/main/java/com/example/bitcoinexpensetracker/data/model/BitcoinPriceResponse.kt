package com.example.bitcoinexpensetracker.data.model

import com.google.gson.annotations.SerializedName

data class BitcoinPriceResponse(
    @SerializedName("bpi") val bpi: Bpi
)

data class Bpi(
    @SerializedName("USD") val usd: CurrencyInfo
)

data class CurrencyInfo(
    @SerializedName("rate_float") val rate: Double
)