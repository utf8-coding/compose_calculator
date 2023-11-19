package com.example.calculator.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculator.dao.CalcHistoryDAO
import com.example.calculator.viewmodel.MainViewModel

/**
 * @projectName: calculator
 * @package: com.example.calculator.ui.components
 * @ComponentName: HistoryView
 * @author: utf8coding
 * @description:
 * @date: 2023/11/18
 */

private val surfacePadding = 10.dp
@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HistoryLayerView(
    viewModel: MainViewModel,
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
                        CalcHistoryDAO(0, "", "1+1", "2"),
                        CalcHistoryDAO(0, "", "100 cos aaa", "80808"),
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
                                modifier = androidx.compose.ui.Modifier
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