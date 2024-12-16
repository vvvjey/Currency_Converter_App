package com.example.currencyconverter.model

data class ExchangeRateResponse(
    val base: String,
    val rates: Map<String, Double>,
    val date: String
)
