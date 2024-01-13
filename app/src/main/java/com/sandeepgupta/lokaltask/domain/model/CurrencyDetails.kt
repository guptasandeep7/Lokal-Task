package com.sandeepgupta.lokaltask.domain.model

data class CurrencyDetails(
    val icon_url: String,
    val max_supply: String,
    val name: String,
    val name_full: String,
    val symbol: String,
    var rate:String
)