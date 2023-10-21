package com.example.calculator.network

import com.example.calculator.dom.CalcHistory
import com.example.calculator.dom.CalcHistoryResponse
import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("get_history")
    fun getCalcHistory(): Call<CalcHistoryResponse>

    @GET("add_history")
    fun addCalcHistory(@Query("expression")expression:String, @Query("result")result:String): Call<ResponseBody>
}