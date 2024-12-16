    package com.example.currencyconverter.model

    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import retrofit2.http.GET
    import retrofit2.http.Query

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
    interface HistoricalApiClient {
        // Lấy tỷ giá theo lịch sử
        @GET("historical")
        suspend fun getHistoricalRates(
            @Query("access_key") accessKey: String,
            @Query("date") date: String,
            @Query("currencies") currencies: String,
            @Query("source") source: String
        ): HistoricalRatesResponse

        companion object {
            private const val BASE_URL = "https://api.exchangerate.host/"

            fun create(): HistoricalApiClient {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(HistoricalApiClient::class.java)
            }
        }
    }
