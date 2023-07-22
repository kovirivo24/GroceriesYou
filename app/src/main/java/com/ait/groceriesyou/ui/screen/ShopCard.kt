package com.ait.groceriesyou.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ait.groceriesyou.R
import com.ait.groceriesyou.ui.data.ShopList.ShopItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShopCard(
    shopItem: ShopItem,
    delState: Boolean = false,
    onLightSurface: Boolean = false,
    shopListViewModel: ShopListViewModel = viewModel(factory = ShopListViewModel.factory),
    onRemoveItem: () -> Unit = {}
) {
    var showOldDialog by rememberSaveable { mutableStateOf(false) }

    if (showOldDialog) {
        shopItemPrompt(
            shopUpdate = true,
            shopItem = shopItem,
            showDialog = showOldDialog,
            onDialogClose = { showOldDialog = false }
        )
    }

    Card(
        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            1.dp,
            getColor(bought = shopItem.bought, lightSurface = onLightSurface, onSurface = false)
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (delState) {
                        shopListViewModel.removeItem(shopItem)
                    } else {
                        showOldDialog = true
                    }
                }
            )
            .padding(
                horizontal = if (!shopItem.bought) 5.dp else 10.dp,
                vertical = if (!shopItem.bought) 5.dp else 3.dp
            )


    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1.3f)
                    .padding(horizontal = 5.dp, vertical = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    shopItem.cat.getIcon(),
                    null,
                    modifier = Modifier
                        .size(if (!shopItem.bought) 45.dp else 38.dp)
                        .padding(end = 15.dp)
                    ,
                    tint =
                    getColor(bought = shopItem.bought, lightSurface = onLightSurface)
                )
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 0.dp, vertical = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var quant = "${shopItem.quantity}"
                    if (quant == "1") quant = ""
                    Text(
                        text = shopItem.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (!shopItem.bought) TextDecoration.None
                        else TextDecoration.LineThrough,
                        fontSize = if (!shopItem.bought) 18.sp else 16.sp,
                        fontWeight = if (!shopItem.bought) FontWeight.Bold else FontWeight.Normal,
                        color = getColor(bought = shopItem.bought,
                            lightSurface = onLightSurface,
                            isTitle = true)
                    )
                    if (!shopItem.bought) {
                        if (shopItem.quantity > 1) {
                            Text(
                                text = "| $quant",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                if (shopItem.desc != "" && !shopItem.bought) {
                    Row() {
                        Text(
                            text = shopItem.desc,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.None,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            }
            Column(
                modifier = Modifier
                    .weight(1.6f)
                    .align(Alignment.CenterVertically)
                    .padding(0.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (shopItem.cost != "0" && shopItem.cost != "") {
                    val newCost = shopItem.cost.toDouble() * shopItem.quantity
                    val costString = String.format("%.2f", newCost)
                    Text(
                        text = costString + " " + stringResource(R.string.ftCostShopCard),
                        textDecoration = if (!shopItem.bought) TextDecoration.None
                        else TextDecoration.LineThrough,
                        fontSize = 15.sp,
                        color = getColor(bought = shopItem.bought,
                            lightSurface = onLightSurface,
                            isMoney = true)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 5.dp,
                        vertical = if (!shopItem.bought) 12.dp else 0.dp
                    )
            ) {
                Checkbox(
                    checked = shopItem.bought,
                    onCheckedChange = {
                        shopListViewModel.updateItemBought(
                            shopItem = shopItem,
                            bought = it
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = getColor(bought = shopItem.bought,
                            lightSurface = onLightSurface,
                            onSurface = false),
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        checkmarkColor = MaterialTheme.colorScheme.background,

                        )
                )
            }
        }
    }
}

@Composable
fun getColor(
    bought: Boolean,
    lightSurface: Boolean,
    onSurface: Boolean = true,
    isMoney: Boolean = false,
    isTitle: Boolean = false): Color {
    return if (bought) {
        if (lightSurface)
            Color.Gray else
                Color.DarkGray
    } else if(onSurface) {
        if (isTitle) MaterialTheme.colorScheme.onSecondaryContainer
        else if (isMoney) Color(0xFF6DC587)
        else MaterialTheme.colorScheme.onSurface
    } else {
        if (lightSurface)
            MaterialTheme.colorScheme.onBackground else
                MaterialTheme.colorScheme.surfaceVariant
    }
}