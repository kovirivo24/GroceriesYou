package com.ait.groceriesyou.ui.screen

import android.R.attr
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ait.groceriesyou.ui.data.ShopList.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Print
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import android.R.attr.maxLines
import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShopListScreen(
    shopListViewModel: ShopListViewModel = viewModel(factory = ShopListViewModel.factory),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val shopList by shopListViewModel.getAllItems().collectAsState(emptyList())
    val itemsByTitle by  shopListViewModel.getAllItemsByTitle().collectAsState(emptyList())
    val itemsByPrice by  shopListViewModel.getAllItemsByPrice().collectAsState(emptyList())
    val usedStoreList by  shopListViewModel.getUsedStores().collectAsState(emptyList())
    val usedCategoriesList by  shopListViewModel.getUsedCategories().collectAsState(emptyList())

    var showNewDialog by rememberSaveable { mutableStateOf(false) }
    var deleteState by rememberSaveable { mutableStateOf(false) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    var searchText by rememberSaveable { mutableStateOf("") }
    var searchActive by rememberSaveable { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false)}
    var selectedSort by remember { mutableStateOf(0) }
    var showBought by remember { mutableStateOf(true)}


    if (showNewDialog) {
        shopItemPrompt(
            shopUpdate = false,
            shopItem = ShopItem(
                title = "",
                quantity = 1,
                cost = "",
                cat = ItemCategory.Misc,
                desc = "",
                store = ItemStore.All
            ),
            showDialog = showNewDialog,
            onDialogClose = { showNewDialog = false }
        )
    }

    Box(Modifier.fillMaxSize()) {
        Box(Modifier
            .semantics { isContainer = true }
            .zIndex(2f)
            .fillMaxWidth()
            .fillMaxHeight()) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = if (searchActive) 0.dp else 5.dp),
                query = if (searchActive) searchText else "",
                onQueryChange = { searchText = it },
                onSearch = { searchActive = false },
                active = searchActive,
                onActiveChange = {
                    searchActive = it
                    searchText = ""
                    deleteState = false
                },
                placeholder = { Text(stringResource(com.ait.groceriesyou.R.string.placeholder_SearchBar)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (!searchActive) {
                        Box {
                            IconButton(onClick = {
                                sortMenuExpanded = true
                            }, modifier = Modifier.padding(15.dp)) {
                                Icon(Icons.Outlined.Sort, null)
                            }
                            DropdownMenu(
                                expanded = sortMenuExpanded,
                                onDismissRequest = {
                                    sortMenuExpanded = false
                                }
                            ) {
                                // adding items
                                shopListViewModel.getSortables().forEachIndexed { itemIndex, itemValue ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedSort = itemIndex
                                            sortMenuExpanded = false
                                        },
                                        enabled = (itemIndex != selectedSort),
                                        text = { Text(itemValue) }
                                    )
                                }
                                Divider()
                                DropdownMenuItem(
                                    onClick = {
                                        showBought = !showBought
                                        sortMenuExpanded = false
                                    },
                                    enabled = true,
                                    text = {Text(text = if (showBought) "Hide Bought"
                                        else "Show bought")}
                                )
                            }
                        }

                    } else {
                        IconButton(
                            onClick = {
                                deleteState = !deleteState
                            },
                            modifier = Modifier
                                .padding(0.dp)
                        ) {
                            Icon(
                                if (!deleteState) Icons.Outlined.Delete
                                else Icons.Filled.Delete, null,
                                tint =
                                if (!deleteState) MaterialTheme.colorScheme.onBackground
                                else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    if (searchText.isNotBlank()) {
                        items(shopList) {
                            if (it.title.contains(searchText, true)
                                || it.desc.contains(searchText, true)) {
                                ShopCard(shopItem = it, delState = deleteState)
                            }
                        }
                    }
                }
            }

        }

        LazyColumn(
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 85.dp, top = 85.dp)
        ) {
            when (selectedSort) {
                0 -> {
                    items(shopList) {
                        if (!showBought) {
                            if (!it.bought) {
                                ShopCard(shopItem = it, delState = deleteState)
                            }
                        }
                        else {
                            ShopCard(shopItem = it, delState = deleteState)
                        }

                    }
                }
                1 -> {
                    items(itemsByTitle) {
                        if (!showBought) {
                            if (!it.bought) {
                                ShopCard(shopItem = it, delState = deleteState)
                            }
                        }
                        else {
                            ShopCard(shopItem = it, delState = deleteState)
                        }
                    }
                }
                2 -> {
                    items(usedCategoriesList) {
                        val itemsByCategory by shopListViewModel.getItemsByCategory(ItemCategory.valueOf(it.s))
                            .collectAsState(emptyList())
                        CategoryCard(it.s, itemsByCategory, deleteState)
                    }
                }
                3 -> {
                    items(usedStoreList) {
                        val itemsByStore by shopListViewModel.getItemsByStore(ItemStore.valueOf(it.s))
                            .collectAsState(emptyList())
                        StoreCard(it.s, itemsByStore, deleteState)
                    }
                }
                4 -> {
                    items(itemsByPrice) {
                        if (!showBought) {
                            if (!it.bought) {
                                ShopCard(shopItem = it, delState = deleteState)
                            }
                        }
                        else {
                            ShopCard(shopItem = it, delState = deleteState)
                        }
                    }
                }
            }
        }
        if (openBottomSheet) {
            MoneyBottomSheet(onDialogClose = { openBottomSheet = false },
                bottomSheetState = bottomSheetState,
                scope = scope,
                viewModel = shopListViewModel
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            BottomAppBar(
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier.zIndex(0f)
                ) {
//                    IconButton(onClick = {
//                        Toast.makeText(context,
//                            context.getString(com.ait.groceriesyou.R.string.settingsWIP_Toast),
//                            Toast.LENGTH_SHORT).show()
//                    }, modifier = Modifier.padding(start = 10.dp)) {
//                        Icon(Icons.Outlined.Settings, null)
//                    }

                    IconButton(onClick = {
                        openBottomSheet = true
                    }, modifier = Modifier.padding(0.dp)) {
                        Icon(Icons.Outlined.Analytics, null)
                    }

                    IconButton(onClick = {
                        deleteState = !deleteState
                    }, modifier = Modifier
                        .padding(0.dp)) {
                        Icon(
                            if (!deleteState) Icons.Outlined.Delete
                            else Icons.Filled.Delete, null,
                            tint =
                            if (!deleteState) MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.error
                        )
                    }

                    IconButton(onClick = {
                        shopListViewModel.clearBoughtItems()
                        Toast.makeText(context,
                            context.getString(com.ait.groceriesyou.R.string.cleared_IconBTN),
                            Toast.LENGTH_SHORT).show()
                    }, modifier = Modifier
                        .padding(horizontal = 0.dp)) {
                        Icon(
                            Icons.Outlined.ClearAll, null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    deleteState = false
                    showNewDialog = true
                },
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 25.dp)
                    .zIndex(1f)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Rounded.Add, null)
            }
        }
    }
}

