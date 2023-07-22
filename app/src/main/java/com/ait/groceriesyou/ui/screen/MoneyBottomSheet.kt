package com.ait.groceriesyou.ui.screen

import android.app.slice.Slice
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import com.ait.groceriesyou.R
import com.ait.groceriesyou.ui.data.ShopList.ItemCategory
import com.ait.groceriesyou.ui.screen.PromptSpinners.StackedBar
import com.ait.groceriesyou.ui.screen.ShopListViewModel.Companion.factory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyBottomSheet(
    onDialogClose: () -> Unit,
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    viewModel: ShopListViewModel
) {
    val totalCost = viewModel.getTotalCost()
    val boughtCost = viewModel.getBoughtCost()
    val autoCost = viewModel.getCostByCategory(ItemCategory.Auto)
    val elecCost = viewModel.getCostByCategory(ItemCategory.Electronics)
    val foodCost = viewModel.getCostByCategory(ItemCategory.Food)
    val homeCost = viewModel.getCostByCategory(ItemCategory.Home)
    val miscCost = viewModel.getCostByCategory(ItemCategory.Misc)

    val categorySlices = listOf(
        com.ait.groceriesyou.ui.data.Slice(
            value = autoCost.toFloat(), color = ItemCategory.Auto.getIconColor(), text = ItemCategory.Auto.name
        ),
        com.ait.groceriesyou.ui.data.Slice(
            value = elecCost.toFloat(), color = ItemCategory.Electronics.getIconColor(), text = ItemCategory.Electronics.name
        ),
        com.ait.groceriesyou.ui.data.Slice(
            value = foodCost.toFloat(), color = ItemCategory.Food.getIconColor(), text = ItemCategory.Food.name
        ),
        com.ait.groceriesyou.ui.data.Slice(
            value = homeCost.toFloat(), color = ItemCategory.Home.getIconColor(), text = ItemCategory.Home.name
        ),
        com.ait.groceriesyou.ui.data.Slice(
            value = miscCost.toFloat(), color = ItemCategory.Misc.getIconColor(), text = ItemCategory.Misc.name
        )
    )

    val boughtSlices = listOf(
        com.ait.groceriesyou.ui.data.Slice(
            value = boughtCost.toFloat(), color = Color(0xFFC3B1E1), text = stringResource(R.string.chart_bought)
        ),
        com.ait.groceriesyou.ui.data.Slice(
            value = (totalCost - boughtCost).toFloat(), color = Color(0xFF77DD77), text = stringResource(
                            R.string.chart_left)
        ),
    )

    ModalBottomSheet(
        onDismissRequest = { onDialogClose() },
        sheetState = bottomSheetState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {

            Row(
                modifier = Modifier.padding(top = 0.dp, bottom = 12.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.chart_totalcost),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 15.dp))
                Text(text = if (totalCost % 1 == 0.0) "${totalCost.toInt()} Ft"
                else "$totalCost Ft",
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier)
            }
            StackedBar(slices = boughtSlices, useCategoryIcons = false)
            StackedBar(slices = categorySlices, useCategoryIcons = true)


            Spacer(modifier = Modifier.padding(10.dp))
//            Text(text = "Store Locations",
//                fontSize = 32.sp,
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp))
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(end = 20.dp, start = 20.dp, bottom = 15.dp),
//                onClick = {}
//            ){
//                Column() {
//                    Text(text = "Spar",
//                        fontSize = 24.sp,
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.SemiBold,
//                        modifier = Modifier.padding(start = 15.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
//                    )
//                    Row(
//                    ){
//                        Text(text = "Open",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 0.dp),
//                            color = Color(0xFF77DD77)
//                        )
//                        Text(text = "Closes 19:00",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 0.dp)
//                        )
//                    }
//                    Text(text = "Rating",
//                        fontSize = 16.sp,
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.padding(start = 20.dp, end = 10.dp, bottom = 10.dp, top = 2.dp)
//                    )
//                }
//            }
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(end = 20.dp, start = 20.dp, bottom = 15.dp),
//                onClick = {}
//            ){
//                Column() {
//                    Text(text = "Auchan",
//                        fontSize = 24.sp,
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.SemiBold,
//                        modifier = Modifier.padding(start = 15.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
//                    )
//                    Row(
//                    ){
//                        Text(text = "Closed",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 0.dp)
//                        )
//                        Text(text = "Closes 16:00",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 0.dp)
//                        )
//                    }
//                    Text(text = "Rating",
//                        fontSize = 16.sp,
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.padding(start = 20.dp, end = 10.dp, bottom = 10.dp, top = 2.dp)
//                    )
//                }
//            }

//            MapView()


        }

    }
}