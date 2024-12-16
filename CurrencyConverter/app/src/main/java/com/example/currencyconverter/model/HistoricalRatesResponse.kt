package com.example.currencyconverter.model

data class HistoricalRatesResponse(
    val success: Boolean,
    val date: String,
    val source: String,
    val quotes: Map<String, Double>
)
