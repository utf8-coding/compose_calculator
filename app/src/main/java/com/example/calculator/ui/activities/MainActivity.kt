package com.example.calculator.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculator.BaseApplication
import com.example.calculator.ui.components.HistoryLayerView
import com.example.calculator.ui.components.KeyBoardView
import com.example.calculator.ui.components.LoanView
import com.example.calculator.ui.components.LoginView
import com.example.calculator.ui.components.NumDisplayCard
import com.example.calculator.ui.components.RateView
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.viewmodel.MainViewModel

private lateinit var viewModel: MainViewModel
private var ansValue: String = ""
private val historyVisibleState = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            BackHandler(enabled = !historyVisibleState.value) {
                this@MainActivity.finish()
            }
            CalculatorTheme {
                val isLogged = remember { mutableStateOf(false) }
                var modeButtonText by remember { mutableStateOf("LOAN") }
                val navController = rememberNavController()
                Column(Modifier.fillMaxSize()) {
                    if (isLogged.value) {
                        Text(
                            modifier = Modifier
                                .height(50.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if (navController.currentDestination?.route == "main_view") {
                                            modeButtonText = "SCI"
                                            navController.navigate("loan_view") {
                                                popUpTo(0)
                                            }
                                            viewModel.getRateRecord { isExist, _ ->
                                                if (!isExist) Toast
                                                    .makeText(
                                                        BaseApplication.appContext,
                                                        "Fetch Rate Record failed",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        } else {
                                            modeButtonText = "LOAN"
                                            navController.navigate("main_view") {
                                                popUpTo(0)
                                            }
                                        }
                                    }
                                )
                                .background(MaterialTheme.colorScheme.surface),
                            text = modeButtonText,
                            style = TextStyle(
                                color = Color(0x88, 0x88, 0x88),
                                fontSize = with(LocalDensity.current) { 22.dp.toSp() },
                                textAlign = TextAlign.Center,
                            ),
                        )
                    }
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "login_view"
                    ) {
                        composable("login_view") {
                            LoginView(isLogged, viewModel, navController) {
                                ansValue = it
                            }
                        }
                        composable("main_view") {
                            MainView()
                        }
                        composable("loan_view") {
                            RateView(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainView() {
    val historyVisibleState = remember { mutableStateOf(false) }
    val textFieldValueState = remember { mutableStateOf("") }
    BackHandler(
        enabled = historyVisibleState.value
    ) {
        historyVisibleState.value = false
    }
    Surface(
        modifier =
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            NumDisplayCard(textFieldValueState)
            KeyBoardView(ansValue, viewModel, historyVisibleState, textFieldValueState)
        }
    }
    HistoryLayerView(viewModel, visible = historyVisibleState)
}