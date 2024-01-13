package com.sandeepgupta.lokaltask.domain

import com.sandeepgupta.lokaltask.domain.model.CombinedCurrencyList
import com.sandeepgupta.lokaltask.util.ApiState
import kotlinx.coroutines.flow.Flow

interface Repository {

    // To get the list of currency with their details and rate
    fun getCombineCurrencyList(): Flow<ApiState<CombinedCurrencyList>>
}