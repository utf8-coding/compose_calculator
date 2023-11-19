package com.example.calculator.network

import android.util.Log
import android.widget.Toast
import com.example.calculator.BaseApplication
import com.example.calculator.dao.CalcHistoryDAO
import com.example.calculator.dao.RateRecordDAO
import com.example.calculator.dao.UserDAO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @projectName: calculator
 * @package: com.example.calculator.network
 * @className: RequestFactory
 * @author: utf8coding
 * @description: Sending requests
 * @date: 2023/11/18
 */
object RequestFactory {
    private val service = ServiceCreator.create<AppService>()

    // 适Kotlin化改造
    private fun <T> Call<T>.makeNormalRequest(
        responseErrorHint: String = "",
        networkErrorHint: String = "",
        onResult: (call: Call<T>, response: Response<T>) -> Unit = { _, _ -> },
        onError: (call: Call<T>, t: Throwable) -> Unit = { _, _ -> }
    ) {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (responseErrorHint != "" && response.code() != 200) {
                    Toast.makeText(
                        BaseApplication.appContext,
                        "Response Error: $responseErrorHint failed, error code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onResult(call, response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (networkErrorHint != "") {
                    Toast.makeText(
                        BaseApplication.appContext,
                        "Network Error: $networkErrorHint failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("utf8coding: [network error] ", t.message.toString())
                }
                onError(call, t)
            }
        })
    }

    fun login(
        userName: String,
        pswd: String,
        onResult: (isSuccess: Boolean, result: UserDAO?) -> Unit,
    ) {
        service.login(userName, pswd).makeNormalRequest(
            onResult = { _, rsp ->
                if (rsp.code() == 501) onResult(false, null)
                else if (rsp.code() == 200) onResult(
                    true,
                    rsp.body()?.data
                )
            },
            networkErrorHint = "login"
        )
    }

    fun register(
        userName: String,
        pswd: String,
        onResult: (isNameOk: Boolean, isInfoLegal: Boolean, result: UserDAO?) -> Unit
    ) {
        service.register(userName, pswd).makeNormalRequest(
            onResult = { _, rsp ->
                when (rsp.code()) {
                    501 ->
                        onResult(false, true, null)

                    502 ->
                        onResult(true, false, null)

                    200 ->
                        onResult(true, true, rsp.body()?.data)
                }
            }
        )
    }

    fun addHistory(userId:String, expression: String, result: String) {
        service.addCalcHistory(userId, expression, result).makeNormalRequest(
            responseErrorHint = "history upload",
            networkErrorHint = "history upload"
        )
    }

    fun getHistories(userId:String, onResult: (result: ArrayList<CalcHistoryDAO>) -> Unit) {
        service.getCalcHistories(userId).makeNormalRequest(
            networkErrorHint = "history fetch",
            onResult = { _, response ->
                val data = response.body()?.data ?: arrayListOf()
                onResult(data)
            }
        )
    }

    fun saveRateRecord(rateRecord: RateRecordDAO, onIllegal: () -> Unit) {
        service.saveRate(rateRecord).makeNormalRequest(
            networkErrorHint = "saveRateRecord",
            responseErrorHint = "saveRateRecord",
            onResult = { _, rsp ->
                if (rsp.code() == 400) {
                    onIllegal()
                }
            }
        )
    }

    fun getRateRecord(
        userId: String,
        onResult: (isExist: Boolean, result: RateRecordDAO?) -> Unit
    ) {
        service.getRate(userId).makeNormalRequest(
            onResult = { _, rsp ->
                if (rsp.code() == 200) {
                    val userInfo = rsp.body()?.data
                    onResult(true, userInfo)
                } else if (rsp.code() == 400) {
                    onResult(false, null)
                }
            },
            networkErrorHint = "getRateRecord"
        )
    }
}