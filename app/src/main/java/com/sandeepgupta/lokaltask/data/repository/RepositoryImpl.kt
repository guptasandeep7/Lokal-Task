package com.sandeepgupta.lokaltask.data.repository

import android.util.Log
import com.sandeepgupta.lokaltask.domain.model.CombinedCurrencyList
import com.sandeepgupta.lokaltask.domain.model.CurrencyListResponse
import com.sandeepgupta.lokaltask.domain.model.RateListResponse
import com.sandeepgupta.lokaltask.domain.Repository
import com.sandeepgupta.lokaltask.data.remote.ApiService
import com.sandeepgupta.lokaltask.util.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {

    private val TAG = "Repository Impl"

    override fun getCombineCurrencyList(): Flow<ApiState<CombinedCurrencyList>> =
        flow {
            emit(ApiState.Loading())
            try {
                val currencyListResponse = apiService.getCurrencyList()
                val rateListResponse = apiService.getRateList()

                Log.d(TAG, "currencyListResponse: $currencyListResponse")
                Log.d(TAG, "rateListResponse: $rateListResponse")

                // wait for both the responses then check and merge
                if (currencyListResponse.success && rateListResponse.success) {
                    val combinedCurrencyList = combineData(currencyListResponse, rateListResponse)
                    emit(ApiState.Success(combinedCurrencyList))

                } else if (!currencyListResponse.success) {
                    emit(ApiState.Error(currencyListResponse.error.type))
                } else {
                    emit(ApiState.Error(rateListResponse.error.type))
                }

            } catch (e: Exception) {
                emit(ApiState.Error(e.message.toString()))
            }
        }

    // To put rate value from rate api to currency details
    private fun combineData(
        currencyListResponse: CurrencyListResponse,
        rateListResponse: RateListResponse
    ): CombinedCurrencyList {

        // round off to 6 decimal place
        currencyListResponse.crypto.forEach {
            it.value.rate = String.format("%.6f", rateListResponse.rates[it.key]?.toDouble())
        }

        return CombinedCurrencyList(
            System.currentTimeMillis(), // current timestamp in Long
            rateListResponse.target, // Targeted currency (USD)
            currencyListResponse.crypto.values.toList() // convert map to list
        )

    }


}

