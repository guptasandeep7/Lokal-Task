package com.sandeepgupta.lokaltask.domain.model

data class CurrencyListResponse(
    val success:Boolean,
    val crypto:Map<String, CurrencyDetails>,
    val fiat:Map<String,String>,
    val error: Error
)
