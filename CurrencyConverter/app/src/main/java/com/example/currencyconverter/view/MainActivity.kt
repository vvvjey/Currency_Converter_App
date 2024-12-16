package com.example.currencyconverter.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.R
import com.example.currencyconverter.controller.CurrencyViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import java.util.logging.Handler
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var fromRateCurrencySelected: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fromCurrencySelect: ConstraintLayout = findViewById(R.id.currencySelectedFrom)
        val toCurrencyViewModel: ConstraintLayout = findViewById(R.id.currencySelectedTo)
        val buttonConvert: Button = findViewById(R.id.buttonConvert)
        val inputAmountFrom: EditText = findViewById(R.id.inputAmountFrom)
        val inputAmountTo: EditText = findViewById(R.id.inputAmountTo)
         fromRateCurrencySelected = findViewById(R.id.exchangeRateText)

        viewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

        viewModel.fetchExchangeRates()
        viewModel.errorLiveData.observe(this, { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Log.e("Error", errorMessage)
                showErrorDialog(errorMessage)
            }
        })
        var fromCurrency = "USD"
        var toCurrency = "USD"

        viewModel.currencyList.observe(this, { currencyList ->
            fromCurrencySelect.setOnClickListener(View.OnClickListener {
                Log.d("currencyList", "Selected currencies: ${currencyList.joinToString(", ")}")
                showCurrencyListOverlay(currencyList, "From", { selectedCurrency ->
                    fromCurrency = selectedCurrency
                    val currencyTextView: TextView = findViewById(R.id.currency1)
                    currencyTextView.text = selectedCurrency
                    updateExchangeRate(fromCurrency, toCurrency, viewModel.exchangeRates.value)
                    viewModel.fetchHistoricalRatesForFiveYears(fromCurrency, toCurrency)


                })
            })

            toCurrencyViewModel.setOnClickListener(View.OnClickListener {
                Log.d("currencyList", "Selected currencies: ${currencyList.joinToString(", ")}")
                showCurrencyListOverlay(currencyList, "To", { selectedCurrency ->
                    toCurrency = selectedCurrency
                    val currencyTextView: TextView = findViewById(R.id.currency2)
                    currencyTextView.text = selectedCurrency
                    updateExchangeRate(fromCurrency, toCurrency, viewModel.exchangeRates.value)


                    viewModel.fetchHistoricalRatesForFiveYears(fromCurrency, toCurrency)




                    val amountFromStr = inputAmountFrom.text.toString()
                    val amountFrom = amountFromStr.toDoubleOrNull()
                    if (amountFrom != null && amountFrom > 0) {
                        val conversionRate = getConversionRate(fromCurrency, toCurrency)
                        val convertedAmount = amountFrom * conversionRate

                        val decimalFormat = DecimalFormat("#,###.##")
                        val formattedAmount = decimalFormat.format(convertedAmount)

                        inputAmountTo.setText(formattedAmount)
                    }
                })
            })
        })
        viewModel.historicalRates.observe(this, { rates ->
            Log.d("DrawHistoricalRates", "Historical rates: $rates")
            drawChart(rates,fromCurrency,toCurrency)
        })



        buttonConvert.setOnClickListener {
            val amountFromStr = inputAmountFrom.text.toString()

            if (amountFromStr.isBlank()) {
                showErrorDialog("Please enter a valid amount to convert.")
                return@setOnClickListener
            }

            val amountFrom = amountFromStr.toDoubleOrNull()

            if (amountFrom == null || amountFrom <= 0) {
                showErrorDialog("Invalid amount entered. Please enter a positive number.")
                return@setOnClickListener
            }
            showLoading()

            Log.d("Convert", "Amount: $amountFrom, From: $fromCurrency, To: $toCurrency")

            android.os.Handler().postDelayed({
                // Thực hiện các bước chuyển đổi
                val conversionRate = getConversionRate(fromCurrency, toCurrency)
                val convertedAmount = amountFrom * conversionRate
                val decimalFormat = DecimalFormat("#,###.##")
                val formattedAmount = decimalFormat.format(convertedAmount)
                inputAmountTo.setText(formattedAmount)

                hideLoading()
            }, 1500)
        }

    }

    private fun getConversionRate(fromCurrency: String, toCurrency: String): Double {
        val rates = viewModel.exchangeRates.value ?: emptyMap()
        Log.d("ExchangeRates", "Rates: $rates")

        val fromRate = rates[fromCurrency] ?: 1.0
        val toRate = rates[toCurrency] ?: 1.0

        if (fromRate != 0.0) {
            return toRate / fromRate
        } else {
            return 1.0
        }
    }


    private fun showCurrencyListOverlay(currencyList: List<String>, currencyType: String, onCurrencySelected: (String) -> Unit) {
        val inflater = LayoutInflater.from(this)
        val overlayView = inflater.inflate(R.layout.adapter_currency_container, null)

        val listView: ListView = overlayView.findViewById(R.id.currencyListView)
        val closeButton: Button = overlayView.findViewById(R.id.closeButton)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencyList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedCurrency = currencyList[position]
            onCurrencySelected(selectedCurrency) // Pass selected currency to the callback
            (overlayView.parent as? ViewGroup)?.removeView(overlayView)
        }

        closeButton.setOnClickListener {
            (overlayView.parent as? View)?.let { parentView ->
                (parentView as? ViewGroup)?.removeView(overlayView)
            }
        }

        addContentView(
            overlayView,
            ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        )
    }
    private fun updateExchangeRate(fromCurrency: String, toCurrency: String, rates: Map<String, Double>?) {
        if (rates != null) {
            val fromRate = rates[fromCurrency] ?: 1.0
            val toRate = rates[toCurrency] ?: 1.0

            val conversionRate = toRate / fromRate

            val exchangeRateTextStr = "1 $fromCurrency = ${String.format("%.2f", conversionRate)} $toCurrency"
            fromRateCurrencySelected.text = exchangeRateTextStr
        }
    }
    private fun showErrorDialog(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Convert Currency Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }

    private fun showLoading() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
    }
    fun drawChart(rates: List<Double>, fromCurrency: String, toCurrency: String) {
        val lineChart: LineChart = findViewById(R.id.lineChart)

        val years = listOf("2019", "2020", "2021", "2022", "2023", "2024")

        val reversedRates = rates.reversed()

        val entries = mutableListOf<Entry>()
        for (i in reversedRates.indices) {
            entries.add(Entry(i.toFloat(), reversedRates[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "$fromCurrency to $toCurrency")
        dataSet.color = resources.getColor(R.color.blue)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in years.indices) {
                        years[index]
                    } else {
                        ""
                    }
                }
            }
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
        }

        lineChart.axisLeft.apply {
            axisMinimum = 0f
            setGranularityEnabled(true)

            val maxRate = reversedRates.maxOrNull() ?: 0.0
            val minRate = reversedRates.minOrNull() ?: 0.0
            axisMaximum = (maxRate * 1.1).toFloat()
            axisMinimum = (minRate * 0.9).toFloat()
        }
        lineChart.axisRight.isEnabled = false

        lineChart.description.isEnabled = true
        lineChart.description.text = "Historical Exchange Rate ($fromCurrency to $toCurrency) (2019 - 2024)"

        lineChart.invalidate()
    }



}

