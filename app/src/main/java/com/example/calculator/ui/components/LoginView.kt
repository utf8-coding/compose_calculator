package com.example.calculator.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.calculator.BaseApplication
import com.example.calculator.dao.CalcHistoryDAO
import com.example.calculator.viewmodel.MainViewModel
import dev.souravdas.materialsegmentedbutton.SegmentedButton
import dev.souravdas.materialsegmentedbutton.SegmentedButtonDefaults
import dev.souravdas.materialsegmentedbutton.SegmentedButtonItem

/**
 * @projectName: calculator
 * @package: com.example.calculator.ui.components
 * @ViewName: LoginView
 * @author: utf8coding
 * @description: Overall login view
 * @date: 2023/11/18
 */

@Preview(
    showBackground = true
)
@Composable
fun LoginView(isLogged: MutableState<Boolean> = mutableStateOf(false), viewModel: MainViewModel = MainViewModel(), navController: NavHostController? = null, onGetANS: (value: String) -> Unit = { _ ->}){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isLoginError by remember{ mutableStateOf(false) }
        var isRegisterNameError by remember{ mutableStateOf(false) }
        var userNameText by remember { mutableStateOf("") }
        var pswdText by remember { mutableStateOf("") }
        var isLogin by remember{ mutableStateOf(true) }
        Spacer(Modifier.height(180.dp))
        SegmentedButton(
            modifier = Modifier.width(330.dp),
            cornerRadius = SegmentedButtonDefaults.segmentedButtonCorners(100),
            items = listOf(
                SegmentedButtonItem(
                    title = {
                        Text(
                            text = "Login",
                            style = TextStyle(fontSize = with(LocalDensity.current){18.dp.toSp()})
                        )
                    },
                    onClick = {
                        isLogin = true
                    }
                ),
                SegmentedButtonItem(
                    title = {
                        Text(
                            text = "Register",
                            style = TextStyle(fontSize = with(LocalDensity.current){18.dp.toSp()})
                        )
                    },
                    onClick = {
                        isLogin = false
                    }
                )
            )
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            value = userNameText,
            onValueChange = {
                if(isLoginError) isLoginError = false
                userNameText = it
            },
            label = {
                Text(text = "User Name")
            },
            isError = isLoginError || isRegisterNameError,
            supportingText = {
                if (isLoginError){
                    Text(text = "Wrong username or password")
                }
                if (isRegisterNameError) {
                    Text(text = "Duplicated username / illegal input")
                }
            }
        )
        Spacer(modifier = Modifier.height(23.dp))
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            value = pswdText,
            onValueChange = {
                if(isLoginError) isLoginError = false
                pswdText = it
            },
            label = {
                Text(
                    text = "Password"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isLoginError || isRegisterNameError,
            supportingText = {
                if (isLoginError){
                    Text(text = "Wrong username or password")
                }
                if (isRegisterNameError) {
                    Text(text = "Duplicated username / illegal input")
                }
            }
        )
        Spacer(modifier = Modifier.height(45.dp))
        if (isLogin) {
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp),
                onClick = {
                    viewModel.login(userNameText, pswdText){ isSuccess, resultDAO ->
                        if (isSuccess) {
                            if (resultDAO == null) {
                                Toast.makeText(
                                    BaseApplication.appContext,
                                    "unknown error, login, resultDAO",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            } else {
                                BaseApplication.appVarHolder.uid = resultDAO.uuid
                                viewModel.rateRecord.uuid = resultDAO.uuid
                                navController!!.navigate("main_view"){
                                    viewModel.getCalcHistory {
                                        Toast.makeText(BaseApplication.appContext, "get ANS success", Toast.LENGTH_SHORT).show()
                                        val history =
                                            it.getOrNull(it.size - 1) ?: CalcHistoryDAO(uuid = BaseApplication.appVarHolder.uid)
                                        onGetANS(history.result)
                                    }
                                    isLogged.value = true
                                    popUpTo(0)
                                }
                                viewModel.getRateRecord{ _, _ -> }
                            }
                        } else {
                            isLoginError = true
                        }
                    }
                }
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(fontSize = with(LocalDensity.current){18.dp.toSp()})
                )
            }
        } else {
            OutlinedButton(
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp),
                onClick = {
                    viewModel.register(userNameText, pswdText){ isNameOK, isInfoLegal, resultDAO ->
                        if (isNameOK && isInfoLegal) {
                            if (resultDAO == null) {
                                Toast.makeText(
                                    BaseApplication.appContext,
                                    "unknown error, login, resultDAO",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            } else {
                                BaseApplication.appVarHolder.uid = resultDAO.uuid
                                navController!!.navigate("main_view"){
                                    popUpTo(0)
                                }
                                isLogged.value = true
                            }
                        } else {
                            isRegisterNameError = true
                        }
                    }
                }
            ) {
                Text(
                    text = "Register",
                    style = TextStyle(fontSize = with(LocalDensity.current){18.dp.toSp()})
                )
            }
        }
    }
}