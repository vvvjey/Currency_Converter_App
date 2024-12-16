package com.example.currencyconverter.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.model.ApiClient
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    private val _currencyList = MutableLiveData<List<String>>()
    val currencyList: LiveData<List<String>> get() = _currencyList

    val errorLiveData = MutableLiveData<String>()


    private val apiClient = ApiClient.create()

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
}
