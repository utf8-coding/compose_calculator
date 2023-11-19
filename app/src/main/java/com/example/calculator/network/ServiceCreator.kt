package com.example.calculator.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val BASE_URL = "http://192.168.148.84:2333/"
    private val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}