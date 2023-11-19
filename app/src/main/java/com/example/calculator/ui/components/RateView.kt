package com.example.calculator.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.viewmodel.MainViewModel
import dev.souravdas.materialsegmentedbutton.SegmentedButton
import dev.souravdas.materialsegmentedbutton.SegmentedButtonDefaults
import dev.souravdas.materialsegmentedbutton.SegmentedButtonItem

/**
 * @projectName: calculator
 * @package: com.example.calculator.ui.components
 * @className: RateView
 * @author: utf8coding
 * @description: View for calculating loan/depo rate
 * @date: 2023/11/19
 */

@Composable
fun LoanView(viewModel: MainViewModel = MainViewModel()) {
    val sixMonthFieldText = remember { mutableStateOf(viewModel.rateRecord.loanRate6.toString()) }
    val oneYearFieldText = remember { mutableStateOf(viewModel.rateRecord.loanRate12.toString()) }
    val one2ThreeYearFieldText =
        remember { mutableStateOf(viewModel.rateRecord.loanRate1236.toString()) }
    val three2FiveYearFieldText =
        remember { mutableStateOf(viewModel.rateRecord.loanRate3660.toString()) }
    val fiveYearFieldText = remember { mutableStateOf(viewModel.rateRecord.loanRate60.toString()) }
    var isInputIllegal by remember { mutableStateOf(false) }

    val nameList = listOf("Six Month", "One Year", "1-3 Years", "3-5 Years", "Five Years")
    val varList = listOf(
        sixMonthFieldText,
        oneYearFieldText,
        one2ThreeYearFieldText,
        three2FiveYearFieldText,
        fiveYearFieldText
    )

    var allowChange by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Loan Rate", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        for (i in nameList.indices) {
            ListItem(
                modifier = Modifier.padding(horizontal = 15.dp),
                headlineContent = {
                    Text(
                        text = nameList[i],
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                trailingContent = {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(130.dp)
                            .height(60.dp),
                        value = varList[i].value,
                        onValueChange = {
                            if (isInputIllegal) isInputIllegal = false
                            varList[i].value = it
                        },
                        suffix = { Text("%") },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        isError = isInputIllegal,
                        readOnly = !allowChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (allowChange) {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(50.dp), onClick = {
                allowChange = false
                viewModel.rateRecord.loanRate6 = sixMonthFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.loanRate12 = oneYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.loanRate1236 =
                    one2ThreeYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.loanRate3660 =
                    three2FiveYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.loanRate60 = fiveYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.updateRateRecord {
                    isInputIllegal = true
                }
            }) {
                Text(text = "Save", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Button(modifier = Modifier
                .width(200.dp)
                .height(50.dp), onClick = { allowChange = true }) {
                Text(text = "Edit", style = MaterialTheme.typography.titleMedium)
            }
        }

    }
}


@Composable
fun DepoView(viewModel: MainViewModel = MainViewModel()) {
    val currentFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRateLive.toString()) }
    val threeMFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate3.toString()) }
    val sixMFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate6.toString()) }
    val oneYearFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate12.toString()) }
    val twoYearFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate24.toString()) }
    val threeYearFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate36.toString()) }
    val fiveYearFieldText = remember { mutableStateOf(viewModel.rateRecord.depoRate60.toString()) }
    var isInputIllegal by remember { mutableStateOf(false) }

    val nameList = listOf(
        "Current Deposit",
        "Three Month",
        "Six Month",
        "One Year",
        "Two Years",
        "Three Years",
        "Five Years"
    )
    val varList = listOf(
        currentFieldText,
        threeMFieldText,
        sixMFieldText,
        oneYearFieldText,
        twoYearFieldText,
        threeYearFieldText,
        fiveYearFieldText
    )

    var allowChange by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Deposit Rate", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        for (i in nameList.indices) {
            ListItem(
                modifier = Modifier.padding(horizontal = 15.dp),
                headlineContent = {
                    Text(
                        text = nameList[i],
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                trailingContent = {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(130.dp)
                            .height(60.dp),
                        value = varList[i].value,
                        onValueChange = {
                            if (isInputIllegal) isInputIllegal = false
                            varList[i].value = it
                        },
                        suffix = { Text("%") },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        isError = isInputIllegal,
                        readOnly = !allowChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (allowChange) {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(50.dp), onClick = {
                allowChange = false
                viewModel.rateRecord.depoRateLive = currentFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate3 = threeMFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate6 = sixMFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate12 = oneYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate24 = twoYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate36 = threeYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.rateRecord.depoRate60 = fiveYearFieldText.value.toDoubleOrNull() ?: 0.0
                viewModel.updateRateRecord {
                    isInputIllegal = true
                }
            }) {
                Text(text = "Save", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Button(modifier = Modifier
                .width(200.dp)
                .height(50.dp), onClick = { allowChange = true }) {
                Text(text = "Edit", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RateView(viewModel: MainViewModel = MainViewModel()) {
    var isInputError by remember { mutableStateOf(false) }
    var timeFieldText by remember { mutableStateOf("") }
    var moneyFieldText by remember { mutableStateOf("") }
    var resultValue by remember { mutableDoubleStateOf(0.0) }
    var isLoanMode by remember { mutableStateOf(true) }
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            SegmentedButton(
                modifier = Modifier.width(330.dp),
                cornerRadius = SegmentedButtonDefaults.segmentedButtonCorners(100),
                items = listOf(
                    SegmentedButtonItem(
                        title = {
                            Text(
                                text = "Loan",
                                style = TextStyle(fontSize = with(LocalDensity.current) { 18.dp.toSp() })
                            )
                        },
                        onClick = {
                            isLoanMode = true
                            resultValue = 0.0
                        }
                    ),
                    SegmentedButtonItem(
                        title = {
                            Text(
                                text = "Deposit",
                                style = TextStyle(fontSize = with(LocalDensity.current) { 18.dp.toSp() })
                            )
                        },
                        onClick = {
                            isLoanMode = false
                            resultValue = 0.0
                        }
                    )
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier.width(330.dp),
                value = timeFieldText,
                onValueChange = {
                    if (isInputError) isInputError = false
                    timeFieldText = it
                },
                suffix = {
                    Text(text = "year")
                },
                label = {
                    Text(text = "Time")
                },
                isError = isInputError,
                supportingText = {
                    if (isInputError) {
                        Text(text = "Illegal input")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(
                modifier = Modifier.width(330.dp),
                value = moneyFieldText,
                onValueChange = {
                    if (isInputError) isInputError = false
                    moneyFieldText = it
                },
                label = {
                    Text(text = "Money")
                },
                isError = isInputError,
                supportingText = {
                    if (isInputError) {
                        Text(text = "Illegal input")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Card(
                Modifier
                    .width(280.dp)
                    .height(80.dp),
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Text(text = String.format("%.4f", resultValue), style = MaterialTheme.typography.headlineSmall)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    resultValue = viewModel.calcMoney(isLoanMode, timeFieldText, moneyFieldText)
                }) {
                Text(text = "Calculate", style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Divider()
        LazyColumn(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                if (isLoanMode) {
                    LoanView(viewModel)
                } else {
                    DepoView(viewModel)
                }

                Spacer(modifier = Modifier.height(25.dp))
            }

        }
    }
}
