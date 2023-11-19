package com.example.calculator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.calculator.BaseApplication
import com.example.calculator.dao.CalcHistoryDAO
import com.example.calculator.dao.RateRecordDAO
import com.example.calculator.dao.UserDAO
import com.example.calculator.network.RequestFactory
import io.kaen.dagger.ExpressionParser


class MainViewModel:ViewModel() {

    var rateRecord: RateRecordDAO = RateRecordDAO()
    fun login(userName: String, pswd: String, onResult: (isSuccess: Boolean, result: UserDAO?) -> Unit) {
        RequestFactory.login(userName, pswd, onResult)
    }

    fun register(userName: String, pswd: String, onResult: (isNameOk: Boolean, isInfoLegal: Boolean, result: UserDAO?) -> Unit) {
        RequestFactory.register(userName, pswd, onResult)
    }
    fun saveCalcHistory(instruction: String, result: String){
        RequestFactory.addHistory(BaseApplication.appVarHolder.uid, instruction, result)
    }

    fun getCalcHistory(onResult: (result: ArrayList<CalcHistoryDAO>) -> Unit){
        RequestFactory.getHistories(BaseApplication.appVarHolder.uid, onResult)
    }

    fun updateRateRecord(onIllegal: () -> Unit) {
        RequestFactory.saveRateRecord(rateRecord, onIllegal)
        Log.e("utf8", "DAO UUID: ${rateRecord.uuid}")
    }

    fun getRateRecord(onResult: (isExist: Boolean, result: RateRecordDAO?) -> Unit) {
        RequestFactory.getRateRecord(BaseApplication.appVarHolder.uid) { isExist, result ->
            if (isExist) {
                rateRecord = result!!
            }
            onResult(isExist, result)
        }
    }

    fun calcMoney(isLoanMode: Boolean, time: String, money: String): Double{
        val timeInMonth = (time.toDoubleOrNull()?:0.0) *12
        val moneyValue = money.toDoubleOrNull()?:0.0
        val loanTimeList = listOf(0, 6, 12, 36, 60)
        val loanRateList = listOf(rateRecord.loanRate6, rateRecord.loanRate12, rateRecord.loanRate1236, rateRecord.loanRate3660, rateRecord.loanRate60)
        val depoTimeList = listOf(0, 3, 6, 12, 24, 36, 60)
        val depoRateList = listOf(rateRecord.depoRateLive, rateRecord.depoRate3, rateRecord.depoRate6, rateRecord.depoRate12, rateRecord.depoRate24, rateRecord.depoRate36, rateRecord.depoRate60)
        var result = 0.0
        if(isLoanMode){
            for (i in loanTimeList.indices){
                if (timeInMonth <= loanTimeList[i]){
                    result = (loanRateList[i-1]*0.01)*moneyValue*time.toDouble()
                    return result
                }
            }
        } else {
            for (i in depoTimeList.indices){
                if (timeInMonth <= depoTimeList[i]){
                    result = (depoRateList[i-1]*0.01)*moneyValue*time.toDouble()
                    return result
                }
            }
        }
        return result
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