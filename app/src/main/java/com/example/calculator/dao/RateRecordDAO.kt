package com.example.calculator.dao

import com.google.gson.annotations.SerializedName

/**
 * @projectName: calculator
 * @package: com.example.calculator.dao
 * @className: RateRecordDAO
 * @author: utf8coding
 * @description: Loan/Deposit rate DAO
 * @date: 2023/11/18
 */
data class RateRecordDAO(
    @SerializedName("uuid") var uuid: String = "",
    @SerializedName("depo_rate_live") var depoRateLive: Double = 0.5,
    @SerializedName("depo_rate_3") var depoRate3: Double = 2.85,
    @SerializedName("depo_rate_6") var depoRate6: Double = 3.05,
    @SerializedName("depo_rate_12") var depoRate12: Double = 3.25,
    @SerializedName("depo_rate_24") var depoRate24: Double = 4.15,
    @SerializedName("depo_rate_36") var depoRate36: Double = 4.75,
    @SerializedName("depo_rate_60") var depoRate60: Double = 5.25,
    @SerializedName("loan_rate_6") var loanRate6: Double = 5.85,
    @SerializedName("loan_rate_12") var loanRate12: Double = 6.31,
    @SerializedName("loan_rate_12_36") var loanRate1236: Double = 6.40,
    @SerializedName("loan_rate_36_60") var loanRate3660: Double = 6.65,
    @SerializedName("loan_rate_60") var loanRate60: Double = 6.80
)
