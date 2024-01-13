package com.sandeepgupta.lokaltask.domain.model

data class RateListResponse(
    val success:Boolean,
    val terms:String,
    val privacy:String,
    val timestamp:Long,
    val target:String,
    val rates:Map<String,String>,
    val error: Error
)