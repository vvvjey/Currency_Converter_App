package com.example.currencyconverter.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiClient {
    @GET("latest/USD")
    suspend fun getExchangeRates(): ExchangeRateResponse

    companion object {
        private const val BASE_URL = "https://api.exchangerate-api.com/v4/"

        fun create(): ApiClient {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiClient::class.java)
        }
    }
}
