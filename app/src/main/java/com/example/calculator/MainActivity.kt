package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.main.AnimatedCounter
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

private var showSmallNum = mutableStateOf(false)
private var sign = mutableStateOf(0)
private var smallNum = mutableStateOf(0)
private var mainNum = mutableStateOf(0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                MainView(
                    onSignClick = { pressedSign ->
                        if (pressedSign in 1..4 || pressedSign == 7){
                            sign.value = pressedSign
                        } else if (pressedSign == 5){
                            when(sign.value){
                                1 -> mainNum.value = smallNum.value + mainNum.value
                                2 -> mainNum.value = smallNum.value - mainNum.value
                                3 -> mainNum.value = smallNum.value * mainNum.value
                                4 -> if(mainNum.value != 0) mainNum.value = smallNum.value / mainNum.value
                                7 -> mainNum.value = smallNum.value.toDouble()
                                    .pow(mainNum.value.toDouble()).toInt()
                                else -> {}
                            }
                            showSmallNum.value = false
                            sign.value = 0
                        } else if (pressedSign == 6){
                            sign.value = 0
                            smallNum.value = 0
                            showSmallNum.value = false
                            mainNum.value = 0
                        }

                        if(pressedSign in 1..4 || pressedSign ==7){
                            showSmallNum.value = true
                            smallNum.value = mainNum.value
                            mainNum.value = 0
                        }
                    },
                    onNumClick = { pressedNumber ->
                        mainNum.value = mainNum.value * 10 + pressedNumber
                    },
                    onSingleSignClick = {pressedSign ->
                        when(pressedSign){
                            1 -> mainNum.value = sin(mainNum.value.toDouble()).toInt()
                            2 -> mainNum.value = cos(mainNum.value.toDouble()).toInt()
                            3 -> mainNum.value = tan(mainNum.value.toDouble()).toInt()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainView(
    onNumClick: (num: Int) -> Unit = {},
    onSignClick: (sign: Int) -> Unit = {},
    onSingleSignClick: (sign: Int) -> Unit = {}
) {
    Surface(
        modifier =
        Modifier
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NumDisplayCard(showSmallNum, sign, smallNum, mainNum, false)
            KeyBoardView(onNumClick, onSignClick, onSingleSignClick)
        }
    }
}

@Composable
fun NumDisplayCard(
    showSmallNum: MutableState<Boolean> = mutableStateOf(false),
    sign: MutableState<Int> = mutableStateOf(0), //0:non, 1:+, 2:-, 3:*, 4:/, 5:=, 6:AC, 7:^
    smallNum: MutableState<Int> = mutableStateOf(0),
    mainNum: MutableState<Int> = mutableStateOf(0),
    isPreview: Boolean = false
) {
    val backgroundColor: Color
    val trivialTextColor: Color

    remember { sign }
    remember { showSmallNum }
    remember { smallNum }
    remember { mainNum }

    when (isSystemInDarkTheme()) {
        true -> {
            backgroundColor = Color(red = 61, green = 61, blue = 61, alpha = 255)
            trivialTextColor = Color(red = 190, green = 190, blue = 190, alpha = 255)
        }

        else -> {
            backgroundColor = Color(red = 218, green = 217, blue = 217, alpha = 255)
            trivialTextColor = Color(red = 104, green = 104, blue = 104, alpha = 255)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(15.dp),
        color = if (isSystemInDarkTheme()) Color.Black else backgroundColor,
        shape = RoundedCornerShape(5.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(15.dp)
                .background(if (isSystemInDarkTheme()) Color.Black else backgroundColor),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            if (showSmallNum.value || isPreview) {
                Text(
                    text = if (isPreview) "preview num 0" else "${smallNum.value}",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = trivialTextColor
                    )
                )
            }

            Text(
                text =
                if (isPreview) "preview sign + "
                else
                    when (sign.value) {
                        0 -> ""
                        1 -> "+ "
                        2 -> "- "
                        3 -> "* "
                        4 -> "/ "
                        5 -> "= "
                        7 -> "^ "
                        else -> ""
                    },
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                    color = trivialTextColor
                )
            )

            AnimatedCounter(
                count = mainNum,
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 36.sp
                )
            )
        }
    }
}

@Composable
fun KeyBoardView(
    onNumClick: (num: Int) -> Unit = {},
    onSignClick: (sign: Int) -> Unit = {},
    onSingleSignClick: (sign: Int) -> Unit = {}
) {
    Column(
        modifier =
        Modifier
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier =
            Modifier
                .padding(top = 10.dp)
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    content = {
                        Text(
                            text = "sin",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                              onSingleSignClick(1)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = "cos",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 20.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSingleSignClick(2)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = "tan",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSingleSignClick(3)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = "^",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSignClick(7)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
            }
        }

        val signArray = arrayOf("+", "-", "*", "/")
        for (i in 0..2) {
            Surface(
                modifier =
                Modifier
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (j in 1..3) {
                        Button(
                            content = {
                                Text(
                                    text = (i * 3 + j).toString(),
                                    style = TextStyle(
                                        fontSize = LocalDensity.current.run { 22.dp.toSp() }
                                    )
                                )
                            },
                            onClick = {
                                onNumClick(i * 3 + j)
                            },
                            modifier =
                            Modifier
                                .height(80.dp)
                                .width(80.dp)
                        )
                    }
                    Button(
                        content = {
                            Text(
                                text = signArray[i],
                                style = TextStyle(
                                    fontSize = LocalDensity.current.run { 22.dp.toSp() }
                                )
                            )
                        },
                        onClick = {
                            onSignClick(i + 1)
                        },
                        modifier =
                        Modifier
                            .height(80.dp)
                            .width(80.dp)
                    )
                }
            }
        }

        Surface(
            modifier =
            Modifier
                .padding(vertical = 10.dp)
                .background(Color.Transparent)
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    content = {
                        Text(
                            text = "AC",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSignClick(6)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = 0.toString(),
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onNumClick(0)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = "=",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSignClick(5)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Button(
                    content = {
                        Text(
                            text = "/",
                            style = TextStyle(
                                fontSize = LocalDensity.current.run { 22.dp.toSp() }
                            )
                        )
                    },
                    onClick = {
                        onSignClick(4)
                    },
                    modifier =
                    Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}