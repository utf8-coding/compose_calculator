package com.example.calculator.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.dom.CalcHistory
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.viewmodel.MainViewModel

private val surfacePadding = 10.dp
private val roundCornerSize = 10.dp
private lateinit var viewModel: MainViewModel
private var ansValue: String = ""
private val historyVisibleState = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getCalcHistory {
                Toast.makeText(baseContext, "get ANS success", Toast.LENGTH_SHORT).show()

            val history = it.getOrNull(it.size-1)?: CalcHistory("0", "0")
            ansValue = history.result
        }

        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                MainView(historyVisibleState)
            }
        }
    }

    override fun onBackPressed() {
        if(historyVisibleState.value){
            historyVisibleState.value = false
        } else this.onBackPressed()
    }
}

@Composable
fun MainView(historyVisibleState: MutableState<Boolean> = mutableStateOf(false)) {
    remember{historyVisibleState}
    val textFieldValueState = remember { mutableStateOf("") }
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
            KeyBoardView(historyVisibleState, textFieldValueState)
        }
    }
    HistoryLayerView(visible = historyVisibleState)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumDisplayCard(
    textFieldState: MutableState<String>,
    isPreview: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .padding(surfacePadding)
            .height(180.dp),
        shape = RoundedCornerShape(roundCornerSize),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .padding(end = 15.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = if (textFieldState.value.length <= 11) {
                        MaterialTheme.typography.displayLarge.fontSize
                    } else if (textFieldState.value.length <= 15) {
                        MaterialTheme.typography.displayMedium.fontSize
                    } else {
                        MaterialTheme.typography.displaySmall.fontSize
                    },
                    fontStyle = MaterialTheme.typography.displayLarge.fontStyle,
                    textAlign = TextAlign.End
                ),
                enabled = false,
                singleLine = true,
                value = if (isPreview) {
                    "1234, text"
                } else {
                    textFieldState.value
                },
                onValueChange = { value: String ->
                    textFieldState.value = value
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyBoardView(
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
    remember { equalState }

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

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HistoryLayerView(
    visible: MutableState<Boolean>,
    isPreview: Boolean = false
) {
    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(
                        red = 0,
                        green = 0,
                        blue = 0,
                        alpha = 120
                    )
                )
                .fillMaxSize()
                .alpha(1f),
            contentAlignment = Alignment.Center
        ) {
            val historyList =
                if (isPreview)
                    mutableListOf(
                        CalcHistory("1+1", "2"),
                        CalcHistory("100 cos aaa", "80808"),
                    )
                else
                    remember{ mutableListOf() }

            if (!isPreview)
                LaunchedEffect(visible) {
                    viewModel.getCalcHistory {
                        historyList.clear()
                        historyList.addAll(it)
                        Log.i("utf8coding", "historyList change")
                    }
                }

            Surface(
                Modifier
                    .fillMaxSize()
                    .clickable { }
                    .background(Color.Transparent)
                    .clickable {
                        visible.value = false
                    },
                color = Color.Transparent,
            ) {

            }

            Surface(
                Modifier.fillMaxSize(0.8f),
                shape = RoundedCornerShape(15.dp),
                color = MaterialTheme.colorScheme.inverseOnSurface,
            ) {
                Column(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, end = 15.dp)
                            .wrapContentHeight(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "History",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() },
                                color = MaterialTheme.typography.displayMedium.color,
                                textAlign = TextAlign.Center
                            )
                        )
                        Icon(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .combinedClickable(
                                    onClick = {
                                        visible.value = false
                                    },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false)
                                ),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }

                    LazyColumn(
                        userScrollEnabled = true,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(historyList) { item ->
                            Surface(
                                modifier = Modifier
                                    .padding(surfacePadding)
                                    .wrapContentHeight(),
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Column(Modifier.wrapContentHeight()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(surfacePadding * 1.8f),
                                        contentAlignment = Alignment.TopStart
                                    ) {
                                        Text(
                                            text = item.expression,
                                            style = TextStyle(
                                                fontSize = LocalDensity.current.run { 25.dp.toSp() },
                                                color = MaterialTheme.typography.displayMedium.color,
                                                textAlign = TextAlign.Center
                                            ),
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(surfacePadding),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        Text(
                                            modifier = Modifier.offset(x = (-5).dp),
                                            text = item.result,
                                            style = TextStyle(
                                                fontSize = LocalDensity.current.run { 25.dp.toSp() },
                                                color = MaterialTheme.typography.displayMedium.color,
                                                textAlign = TextAlign.Center
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}

@Preview(showBackground = true)
@Composable
fun HistoryLayerPreview() {
    HistoryLayerView(
        remember { mutableStateOf(true) },
        true
    )
}