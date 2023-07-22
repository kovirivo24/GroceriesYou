package com.ait.groceriesyou.ui.screen.PromptSpinners

import android.content.ClipData.Item
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ait.groceriesyou.R
import com.ait.groceriesyou.ui.data.ShopList.ItemCategory
import com.ait.groceriesyou.ui.data.ShopList.ShopItem
import com.ait.groceriesyou.ui.data.Slice
import com.ait.groceriesyou.ui.screen.getColor
import kotlinx.coroutines.delay

@Composable
fun StackedBar(
    slices: List<Slice>,
    useCategoryIcons: Boolean = false
) {

    var state by remember { mutableStateOf(.0f) }
    val animatedSize  = animateFloatAsState(targetValue = state, animationSpec = tween(300))

    LaunchedEffect(key1 = Unit, block = {
        delay(1000L)
        state = 1f
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ){
        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 0.dp, bottom = 0.dp)
                .height(18.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp,
                        bottomEnd = 15.dp,
                        bottomStart = 15.dp
                    )
                ),
        ) {
            slices.forEach {
                if (it.value > 0f) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(it.value),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(it.color),
                            contentAlignment = Alignment.Center

                        ) {
                        }
                    }

                }
            }

        }

        Row(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 0.dp, start = 12.dp)
                .wrapContentHeight()) {
            slices.forEach {
                if (it.value > 0f) {
                    Column(
                        modifier = Modifier
                            .padding(top = 0.dp, bottom = 5.dp, start = 0.dp, end = 10.dp)
                    ) {
                        if (useCategoryIcons){
                                val itemCat = ItemCategory.valueOf(it.text)
                                var newInt: Int = 0
                                if (it.value % 1.0 == 0.0) {
                                    newInt = it.value.toInt()
                                }
                                Icon(
                                    itemCat.getIcon(),
                                    null,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(top = 5.dp)
                                    ,
                                    tint =
                                    it.color
                                )
                                Text(text = if (it.value.toDouble() % 1 == 0.0) it.value.toInt()
                                    .toString() + " " + stringResource(R.string.currency_ft)
                                        else it.value.toString() + " " + stringResource(R.string.currency_ft)
                                    , color = it.color, fontSize = 18.sp)
                        }
                        else {
                            Text(text = it.text, color = it.color, fontSize = 18.sp)
                            Text(text = if (it.value.toDouble() % 1 == 0.0) it.value.toInt()
                                .toString() + " " + stringResource(R.string.currency_ft)
                            else it.value.toString() + " " +
                                    stringResource(R.string.currency_ft), color = it.color, fontSize = 18.sp)
                        }

                    }
                }
            }
        }
    }


}