package com.sandeepgupta.lokaltask.domain.model
data class Error(
    val code:Int,
    val type: String,
    val info:String
)