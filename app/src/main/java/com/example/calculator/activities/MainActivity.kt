package com.example.calculator.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    val textFieldValueState = remember {
        mutableStateOf(
            TextFieldValue(
                text = ""
            )
        )
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
            KeyBoardView(textFieldValueState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumDisplayCard(
    textFieldState: MutableState<TextFieldValue>,
    isPreview: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.padding(surfacePadding),
        shape = RoundedCornerShape(roundCornerSize),
    ) {
        Column{
            Spacer(modifier = Modifier.height(50.dp))
            BasicTextField(
                modifier = Modifier.background(Color.Transparent),
                textStyle = MaterialTheme.typography.displayLarge,
                enabled = false,
                singleLine = true,
                value = if (isPreview) {
                    TextFieldValue("1234, text")
                } else {
                    textFieldState.value
                },
                onValueChange = { value: TextFieldValue ->
                    textFieldState.value = value
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            )
        }
    }
}

@Composable
fun KeyBoardView(
    textFieldState: MutableState<TextFieldValue>
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
    val numpadBtnOperationContent =
        arrayListOf(
            "7", "8", "9", "÷", "del",
            "4", "5", "6", "×", "^",
            "1", "2", "3", "-", "",
            ".", "0", "", "+", "="
        )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Surface (
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = surfacePadding, vertical = surfacePadding),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(roundCornerSize)

        ) {
            Column(Modifier.fillMaxWidth()){
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
                                                textFieldState.value.text.plus(funcBtnInptContent[j + i * 4])
                                            }
                                        ),
                                onClick = {}
                            ){
                                Text(
                                    style = TextStyle(
                                        fontSize = LocalDensity.current.run{18.dp.toSp()}
                                    ),
                                    text = funcBtnDispContent[j+i*4],
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }

        Surface (
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = surfacePadding, vertical = surfacePadding),
            shape = RoundedCornerShape(roundCornerSize)

        )  {
            Column(Modifier.fillMaxWidth()){
                for(i in 0..3){
                    Row(
                        modifier = Modifier
                            .padding(vertical = surfacePadding / 2)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ){
                        for(j in 0..4){
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier =
                                Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(bounded = false),
                                        onClick = {
                                            textFieldState.value.text.plus(numpadBtnOperationContent[j+i*5])
                                        }
                                    )
                                    .weight(20f),
                                onClick = {}
                            ){
                                Text(
                                    style = TextStyle(
                                        fontSize = LocalDensity.current.run{20.dp.toSp()},

                                    ),
                                    text = numpadBtnOperationContent[j+i*5],
                                    color = MaterialTheme.colorScheme.secondary
                                )
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