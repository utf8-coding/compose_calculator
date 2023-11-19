package com.example.calculator.network

import com.example.calculator.dao.ArrayResponse
import com.example.calculator.dao.CalcHistoryDAO
import com.example.calculator.dao.RateRecordDAO
import com.example.calculator.dao.Response
import com.example.calculator.dao.UserDAO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface AppService {
    @GET("login")
    fun login(@Query("user_name")userName: String, @Query("pswd")pswd: String): Call<Response<UserDAO>>

    @PUT("register")
    fun register(@Query("user_name")userName: String, @Query("pswd")pswd: String): Call<Response<UserDAO>>

    @GET("get_rate")
    fun getRate(@Query("user_id")userId: String): Call<Response<RateRecordDAO>>

    @GET("get_histories")
    fun getCalcHistories(@Query("user_id")userId: String): Call<ArrayResponse<CalcHistoryDAO>>

    @PUT("add_history")
    fun addCalcHistory(@Query("user_id")userId: String, @Query("expression")expression:String, @Query("result")result:String): Call<ResponseBody>

    @PUT("add_rate")
    fun saveRate(@Body rateRecord: RateRecordDAO): Call<ResponseBody>
}