package com.example.calculator.dao

import com.google.gson.annotations.SerializedName

data class CalcHistoryDAO(
    @SerializedName("time") val time: Long = 0,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("expression") val expression: String = "0",
    @SerializedName("result") val result: String = "0"
)
