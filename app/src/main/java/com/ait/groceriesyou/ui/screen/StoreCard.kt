package com.ait.groceriesyou.ui.screen

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Expand
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ait.groceriesyou.ui.data.ShopList.ShopItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StoreCard(
    store: String,
    itemsByStore: List<ShopItem>,
    deleteState: Boolean,
    shopListViewModel: ShopListViewModel = viewModel(factory = ShopListViewModel.factory),
    onRemoveItem: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        border = BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(
                horizontal = 0.dp,
                vertical = 10.dp
            )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = store,
                fontSize = 18.sp,
//                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 0.dp, end = 0.dp)

            )
        }
        for (item in itemsByStore) {
            Log.d("storecard", item.title + item.store)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
            ) {
                ShopCard(shopItem = item, delState = deleteState, onLightSurface = true)
            }
        }
    }
}