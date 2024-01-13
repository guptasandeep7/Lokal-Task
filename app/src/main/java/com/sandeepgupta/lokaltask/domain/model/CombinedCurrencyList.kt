package com.sandeepgupta.lokaltask.domain.model

data class CombinedCurrencyList(
    val timestamp:Long,
    val target:String,
    var currencyDetailsList: List<CurrencyDetails>
)
