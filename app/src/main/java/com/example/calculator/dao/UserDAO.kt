package com.example.calculator.dao

import com.google.gson.annotations.SerializedName

/**
 * @projectName: calculator
 * @package: com.example.calculator.dao
 * @className: User
 * @author: utf8coding
 * @description: DAO of User
 * @date: 2023/11/18
 */
data class UserDAO(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("password") val password: String
)
