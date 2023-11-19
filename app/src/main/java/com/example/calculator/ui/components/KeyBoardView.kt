package com.example.calculator.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculator.viewmodel.MainViewModel

/**
 * @projectName: calculator
 * @package: com.example.calculator.ui.components
 * @component: KeyBoardView
 * @author: utf8coding
 * @description: KeyBoard for normal calculator
 * @date: 2023/11/18
 */

private val surfacePadding = 10.dp
private val roundCornerSize = 10.dp
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyBoardView(
    firstAnsValue: String,
    viewModel: MainViewModel,
    historyVisibleState: MutableState<Boolean>,
    textFieldState: MutableState<String>
) {
    val funcBtnDispContent =
        arrayListOf(
            "(  ", "  )", " √ ", "ln ",
            "sin", "cos", "tan", "ANS"
        ) // 4*2
    val funcBtnInptContent =
        arrayListOf(
            "(", ")", "√(", "ln(",
            "sin(", "cos(", "tan(", "ANS"
        )

    val numpadBtnDispContent =
        arrayListOf(
            "7", "8", "9", "÷", "del",
            "4", "5", "6", "×", "^",
            "1", "2", "3", "-", "rem",
            ".", "0", "spc", "+", "="
        ) // 5*4 del, spc, = 是特殊标志，后文进行特殊处理
    val numpadBtnOperation =
        arrayListOf(
            "7", "8", "9", "÷", "del",
            "4", "5", "6", "×", "^",
            "1", "2", "3", "-", "%",
            ".", "0", "", "+", "="
        )

    var equalState = false
    var ansValue = firstAnsValue
    remember { equalState }
    remember{ ansValue }

    Column(
        modifier = Modifier.background(Color.Transparent),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // ---------- Function area ---------- //
        Spacer(modifier = Modifier.height(15.dp))
        Surface(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = surfacePadding, vertical = surfacePadding),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(roundCornerSize)

        ) {
            Column(Modifier.fillMaxWidth()) {
                for (i in 0..1) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .padding(vertical = surfacePadding / 2)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (j in 0..3) {
                            Surface(
                                color = Color.Transparent,
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .combinedClickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            if (funcBtnInptContent[j + i * 4] == "ANS") {
                                                textFieldState.value =
                                                    textFieldState.value + ansValue
                                            } else {
                                                textFieldState.value =
                                                    textFieldState.value + (funcBtnInptContent[j + i * 4])
                                            }
                                        },
                                        onLongClick = {
                                            if (funcBtnInptContent[j + i * 4] == "ANS") {
                                                historyVisibleState.value = true
                                            } else {
                                                textFieldState.value =
                                                    textFieldState.value + (funcBtnInptContent[j + i * 4])
                                            }
                                        }
                                    )
                            ) {
                                val buttonText = funcBtnDispContent[j + i * 4]
                                Text(
                                    style = TextStyle(
                                        fontSize =
                                        if (buttonText == "ANS") {
                                            LocalDensity.current.run { 21.dp.toSp() }
                                        } else {
                                            LocalDensity.current.run { 25.dp.toSp() }
                                        },
                                        color = MaterialTheme.typography.displayMedium.color,
                                        textAlign = TextAlign.Center
                                    ),
                                    text = buttonText
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        // ---------- Numpad area ---------- //
        Surface(
            modifier =
            Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(roundCornerSize)

        ) {
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(25.dp))
                for (i in 0..3) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = surfacePadding * 2)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        for (j in 0..4) {
                            if (numpadBtnDispContent[j + i * 5] == "spc") {
                                Spacer(Modifier.weight(20f))
                            } else {
                                Surface(
                                    modifier =
                                    Modifier
                                        .combinedClickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(bounded = false),
                                            onClick = {
                                                if (numpadBtnOperation[j + i * 5] == "del") {
                                                    if (equalState) {
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(0 until textFieldState.value.length)
                                                        equalState = false
                                                    }
                                                    if (textFieldState.value.isNotEmpty()) {
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(
                                                                textFieldState.value.length - 1 until textFieldState.value.length
                                                            )
                                                    }
                                                } else if (numpadBtnOperation[j + i * 5] == "=") {
                                                    val expression = textFieldState.value
                                                    val result = viewModel
                                                        .calculateExpression(expression)
                                                    textFieldState.value = result
                                                    ansValue = result
                                                    viewModel.saveCalcHistory(expression, result)
                                                    equalState = true
                                                } else {
                                                    if (equalState) {
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(0 until textFieldState.value.length)
                                                        equalState = false
                                                    }
                                                    textFieldState.value =
                                                        textFieldState.value + numpadBtnOperation[j + i * 5]
                                                }
                                            },
                                            onLongClick = {
                                                if (numpadBtnOperation[j + i * 5] == "del") {
                                                    if (textFieldState.value.isNotEmpty()) {
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(0 until textFieldState.value.length)
                                                    }
                                                }
                                            }
                                        )
                                        .weight(20f)
                                        .padding(horizontal = 5.dp)
                                ) {
                                    val text = numpadBtnDispContent[j + i * 5]
                                    Text(
                                        modifier = Modifier.background(Color.Transparent),
                                        style = TextStyle(
                                            fontSize =
                                            if (text == "del" || text == "rem") {
                                                LocalDensity.current.run { 25.dp.toSp() }
                                            } else {
                                                LocalDensity.current.run { 30.dp.toSp() }
                                            },
                                            color = MaterialTheme.typography.displayMedium.color,
                                            textAlign = TextAlign.Center
                                        ),
                                        text = text,
                                        color = MaterialTheme.typography.bodyMedium.color
                                    )
                                }
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
            }

        }
    }
}