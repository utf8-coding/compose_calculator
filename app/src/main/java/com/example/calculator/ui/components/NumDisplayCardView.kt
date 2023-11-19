package com.example.calculator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * @projectName: calculator
 * @package: com.example.calculator.ui.components
 * @componentName: NumDisplayCard
 * @author: utf8coding
 * @description: NumDisplayCard for mainView
 * @date:
 */

private val surfacePadding = 10.dp
private val roundCornerSize = 10.dp
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