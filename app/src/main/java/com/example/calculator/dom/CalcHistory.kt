package com.example.calculator.dom

import com.google.gson.annotations.SerializedName

data class CalcHistory(
    @SerializedName("expression")
    val expression: String,

    @SerializedName("result")
    val result: String
)
