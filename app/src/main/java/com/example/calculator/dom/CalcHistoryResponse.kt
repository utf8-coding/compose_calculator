package com.example.calculator.dom

import com.google.gson.annotations.SerializedName

data class CalcHistoryResponse(
    @SerializedName("data")
    val data: ArrayList<CalcHistory> = arrayListOf(),
)
