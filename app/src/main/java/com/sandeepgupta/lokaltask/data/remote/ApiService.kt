package com.sandeepgupta.lokaltask.data.remote

import com.sandeepgupta.lokaltask.domain.model.CurrencyListResponse
import com.sandeepgupta.lokaltask.domain.model.RateListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.sandeepgupta.lokaltask.BuildConfig


interface ApiService {

    @GET("live")
    suspend fun getRateList(
        @Query("access_key") accessKey: String = BuildConfig.API_KEY
    ): RateListResponse

    @GET("list")
    suspend fun getCurrencyList(
        @Query("access_key") accessKey: String = BuildConfig.API_KEY

    ): CurrencyListResponse

}