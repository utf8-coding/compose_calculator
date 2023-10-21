package com.example.calculator.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.BaseApplication

private val surfacePadding = 10.dp
private val roundCornerSize = 10.dp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                MainView()
            }
        }
    }
}

fun calcString(textFieldString: String, onOutCome: (value: Double, error: Error?) -> Unit) {
    onOutCome(0.0, Error("Not impl, str:$textFieldString"))
}

@Composable
fun MainView() {
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
            KeyBoardView(textFieldValueState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumDisplayCard(
    textFieldState: MutableState<String>,
    isPreview: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier.padding(surfacePadding),
        shape = RoundedCornerShape(roundCornerSize),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(end = 15.dp)
        ){
            Spacer(modifier = Modifier.height(80.dp))
            BasicTextField(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    fontStyle = MaterialTheme.typography.displayLarge.fontStyle,
                    textAlign = TextAlign.End
                ),
                enabled = false,
                singleLine = true,
                value = if (isPreview) {"1234, text"
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
    textFieldState: MutableState<String>
) {
    val funcBtnDispContent =
        arrayListOf(
            "(", ")", "√", "ln",
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
            "1", "2", "3", "-", "spc",
            ".", "0", "spc", "+", "="
        ) // 5*4 del, spc, = 是特殊标志，后文进行特殊处理
    val numpadBtnOperationOperation =
        arrayListOf(
            "7", "8", "9", "÷", "del",
            "4", "5", "6", "×", "^",
            "1", "2", "3", "-", "",
            ".", "0", "", "+", "="
        )

    Column(
        modifier = Modifier.background(Color.Transparent),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Surface (
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = surfacePadding, vertical = surfacePadding),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(roundCornerSize)

        ) {
            Column(Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.height(10.dp))
                for(i in 0..1){
                    Row(
                        modifier = Modifier
                            .padding(vertical = surfacePadding / 2)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ){
                        for(j in 0..3){
                            TextButton(
                                modifier =
                                    Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(bounded = false),
                                            onClick = {
                                                textFieldState.value = textFieldState.value + (funcBtnInptContent[j + i * 4])
                                            }
                                        ),
                                onClick = {}
                            ){
                                Text(
                                    style = TextStyle(
                                        fontSize = LocalDensity.current.run{25.dp.toSp()},
                                        color = MaterialTheme.typography.displayMedium.color
                                    ),
                                    text = funcBtnDispContent[j+i*4]
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        Surface (
            modifier =
            Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(roundCornerSize)

        )  {
            Column(Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.height(25.dp))
                for(i in 0..3){
                    Row(
                        modifier = Modifier
                            .padding(vertical = surfacePadding * 2)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ){
                        for(j in 0..4){
                            if(numpadBtnDispContent[j+i*5] == "spc"){
                                Spacer(Modifier.weight(20f))
                            } else{
                                Surface(
                                    modifier =
                                    Modifier
                                        .combinedClickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(bounded = false),
                                            onClick = {
                                                if (numpadBtnOperationOperation[j + i * 5] == "del") {
                                                    if (textFieldState.value.isNotEmpty()){
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(
                                                                textFieldState.value.length - 1 until textFieldState.value.length
                                                            )
                                                    }
                                                } else if (numpadBtnOperationOperation[j + i * 5] == "=") {

                                                } else {
                                                    textFieldState.value =
                                                        textFieldState.value + numpadBtnOperationOperation[j + i * 5]
                                                }
                                            },
                                            onLongClick = {
                                                if (numpadBtnOperationOperation[j + i * 5] == "del") {
                                                    if (textFieldState.value.isNotEmpty()) {
                                                        textFieldState.value =
                                                            textFieldState.value.removeRange(0 until textFieldState.value.length)
                                                    }
                                                }
                                            }
                                        )
                                        .weight(20f)
                                        .padding(horizontal = 5.dp)
                                ){
                                    val text = numpadBtnDispContent[j+i*5]
                                    Text(
                                        modifier = Modifier.background(Color.Transparent),
                                        style = TextStyle(
                                            fontSize =
                                            if(text == "del") {
                                                LocalDensity.current.run { 25.dp.toSp()}
                                            } else {
                                                LocalDensity.current.run { 30.dp.toSp()}
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

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}