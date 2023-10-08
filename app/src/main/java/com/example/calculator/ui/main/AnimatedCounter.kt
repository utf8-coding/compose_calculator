package com.example.calculator.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    count: MutableState<Int>,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    var oldCount by remember { count }
    var flippo by remember { mutableStateOf(false) }
    SideEffect {
        oldCount = count.value
    }
    Row(modifier = modifier) {
        val countString = "${count.value}"
        val oldCountString = "$oldCount"
        for(i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i) ?: '0'
            val newChar = countString[i]
            val char = if(oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                },
                label = countString
            ) { contentChar ->
                Text(
                    text = contentChar.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}