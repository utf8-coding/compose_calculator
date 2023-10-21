package com.example.calculator.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.calculator.BaseApplication
import com.example.calculator.dom.CalcHistory
import com.example.calculator.dom.CalcHistoryResponse
import com.example.calculator.network.AppService
import com.example.calculator.network.ServiceCreator
import io.kaen.dagger.ExpressionParser
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class MainViewModel:ViewModel() {
    fun saveCalcHistory(instruction: String, result: String){
        netSaveHistory(instruction, result)
    }

    fun getCalcHistory(onResult: (result: ArrayList<CalcHistory>) -> Unit){
        netGetHistory(onResult)
    }
    private fun netSaveHistory(expression: String, result: String){
        val service = ServiceCreator.create<AppService>()
        service.addCalcHistory(expression, result).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.errorBody() != null)
                    Toast.makeText(BaseApplication.appContext, "upload failed", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(BaseApplication.appContext, "history upload failed: ${t.stackTrace}", Toast.LENGTH_LONG).show()
                Log.e("utf8coding", t.stackTrace.toString())
            }
        })
    }
    private fun netGetHistory(onResult: (result: ArrayList<CalcHistory>) -> Unit){
        val service = ServiceCreator.create<AppService>()
        service.getCalcHistory().enqueue(object : Callback<CalcHistoryResponse> {
            override fun onResponse(call: Call<CalcHistoryResponse>, response: Response<CalcHistoryResponse>) {
                val body = response.body()?:CalcHistoryResponse()
                onResult(body.data)
            }

            override fun onFailure(call: Call<CalcHistoryResponse>, t: Throwable) {
                Toast.makeText(BaseApplication.appContext, "history fetch failed: ${t.stackTrace}", Toast.LENGTH_LONG).show()
                Log.e("utf8coding", t.stackTrace.toString())
            }
        })
    }

    // ---------- 计算器字符串解析 ----------//
    fun calculateExpression(input: String): String {
        return try {
            ExpressionParser().evaluate(
                input.replace('×', '*')
                    .replace('÷', '/')
                    .replace("√", "sqrt"),
                4).toString()
        } catch (e: Exception) {
            e.message.toString()
        }
    }
}