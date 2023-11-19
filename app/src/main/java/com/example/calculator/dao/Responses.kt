package com.example.calculator.dao

import com.google.gson.annotations.SerializedName

data class ArrayResponse<T>(
    @SerializedName("data")
    val data: ArrayList<T> = arrayListOf(),
)
data class Response<T>(
    @SerializedName("data")
    val data: T,
)