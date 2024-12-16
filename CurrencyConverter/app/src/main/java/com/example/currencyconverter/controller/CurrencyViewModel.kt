package com.example.currencyconverter.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.model.ApiClient
import com.example.currencyconverter.model.HistoricalApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar

class CurrencyViewModel : ViewModel() {
    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    private val _currencyList = MutableLiveData<List<String>>()
    val currencyList: LiveData<List<String>> get() = _currencyList

    val errorLiveData = MutableLiveData<String>()


    private val apiClient = ApiClient.create()
    private val historicalApiClient = HistoricalApiClient.create()

    private val _historicalRates = MutableLiveData<List<Double>>()
    val historicalRates: LiveData<List<Double>> get() = _historicalRates

    fun fetchExchangeRates() {
        viewModelScope.launch {
            try {
                val response = apiClient.getExchangeRates()
                _exchangeRates.value = response.rates
                _currencyList.value = response.rates.keys.toList()
            } catch (e: Exception) {
//                e.printStackTrace()
                errorLiveData.postValue("Unable convert currency due to network error")

            }
        }
    }

    fun convertCurrency(from: String, to: String): Double {
        val rates = _exchangeRates.value ?: return 0.0
        val fromRate = rates[from] ?: 1.0
        val toRate = rates[to] ?: 1.0
        return toRate / fromRate
    }
    fun fetchHistoricalRatesForFiveYears(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val historicalRatesList = mutableListOf<Double>()

                // Lặp qua 5 năm gần nhất
                for (i in 0..4) {
                    calendar.set(Calendar.YEAR, currentYear - i)
                    val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

                    val response = withContext(Dispatchers.IO) {
                        historicalApiClient.getHistoricalRates(
                            accessKey = "bf4eb89984cd889121d19d363df9f002",
                            date = date,
                            currencies = toCurrency,
                            source = fromCurrency
                        )
                    }

                    if (response.success) {
                        val rate = response.quotes["$fromCurrency$toCurrency"]
                        if (rate != null) {
                            historicalRatesList.add(rate)
                        }
                    } else {
                        Log.e("HistoricalRates", "Failed to fetch rates for year: $date")
                    }
                }

                _historicalRates.value = historicalRatesList

            } catch (e: Exception) {
                Log.e("HistoricalRatesError", "Error: ${e.message}")
            }
        }
    }

}
