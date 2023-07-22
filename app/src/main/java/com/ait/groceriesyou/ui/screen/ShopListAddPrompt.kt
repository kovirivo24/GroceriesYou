package com.ait.groceriesyou.ui.screen

import android.content.ClipData.Item
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ait.groceriesyou.ui.data.ShopList.*
import com.ait.groceriesyou.ui.screen.PromptSpinners.CategorySpinner
import com.ait.groceriesyou.ui.screen.PromptSpinners.StoreSpinner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun shopItemPrompt(
    shopUpdate: Boolean,
    shopItem: ShopItem,
    showDialog: Boolean,
    shopListViewModel: ShopListViewModel = viewModel(factory = ShopListViewModel.factory),
    onDialogClose: () -> Unit
) {
    val context = LocalContext.current
    val allStores = StoreList
    val allCats = CategoryList

    var shopTitle by rememberSaveable { mutableStateOf(shopItem.title) }
//    var listID by rememberSaveable { mutableStateOf(shopItem.ownerid) }
    var shopCost by rememberSaveable { mutableStateOf(shopItem.cost) }
    var shopQuant by rememberSaveable { mutableStateOf(shopItem.quantity) }
    var shopDesc by rememberSaveable { mutableStateOf(shopItem.desc) }
    var shopStore by rememberSaveable { mutableStateOf(shopItem.store) }
    var shopBought by rememberSaveable { mutableStateOf(shopItem.bought) }
    var shopCat by rememberSaveable { mutableStateOf(shopItem.cat) }

    var inputErrorState by rememberSaveable { mutableStateOf(false) }



    fun validateEmpty(text: String) {
        inputErrorState = text.isEmpty()
    }


    Dialog(
        onDismissRequest = onDialogClose,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false
            )
        ,
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 40.dp, vertical = 0.dp),
            shape = RoundedCornerShape(size = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 15.dp, horizontal = 17.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = shopTitle,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 3.dp, end = 3.dp, top = 3.dp),
                        label = {
                            Text(text = stringResource(com.ait.groceriesyou.R.string.shopName_TF))
                        },
                        onValueChange = {
                            shopTitle = it
                            validateEmpty(shopTitle)
                        },
                        isError = inputErrorState,
                        supportingText = {
                            if (inputErrorState) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(com.ait.groceriesyou.R.string.shopTitle_TF),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (inputErrorState)
                                Icon(
                                    Icons.Filled.Warning,
                                    stringResource(com.ait.groceriesyou.R.string.err_IconDesc),
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        keyboardActions = KeyboardActions() {
                            validateEmpty(shopTitle)
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = shopDesc,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 3.dp, end = 3.dp, bottom = 5.dp),
                        label = { Text(text = stringResource(com.ait.groceriesyou.R.string.desc_TF)) },
                        onValueChange = {
                            shopDesc = it
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                }

                val numPattern = remember { Regex("^\\d+\$") }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = shopCost,
                        modifier = Modifier
                            .weight(1.75f)
                            .padding(horizontal = 3.dp, vertical = 5.dp),
                        label = { Text(text = stringResource(com.ait.groceriesyou.R.string.cost_TF)) },
                        onValueChange = {
                            if (it.isEmpty() || it.matches(numPattern)) {
                                shopCost = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = shopQuant.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp, vertical = 5.dp),
                        label = { Text(text = stringResource(com.ait.groceriesyou.R.string.quant_TF)) },
                        onValueChange = {
                            shopQuant = if (it == "") 0 else it.toInt()
                            if (it.length > 1) {
                                if (it.startsWith("0")) {
                                    it.drop(1)
                                }
                                // TODO: prevent ending 0s e.g. prevent 0 + 1 -> 10
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.25f)
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                    ) {
                        CategorySpinner(
                            listCat = allCats,
                            oldCat = shopCat,
                            selectedCat = { newCat -> shopCat = newCat }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                    ) {
                        StoreSpinner(
                            listStores = allStores,
                            oldStore = shopStore,
                            selectedStore = { newStore -> shopStore = newStore }
                        )
                    }
                }

                Row(modifier = Modifier
                    .padding(top = 10.dp)) {
                    Button(onClick = {
                        validateEmpty(shopTitle)

                        if (!inputErrorState) {
                            if (!shopUpdate) {
                                var newShop = ShopItem(
                                    title = shopTitle,
                                    quantity = shopQuant,
                                    cost = shopCost,
                                    cat = shopCat,
                                    desc = shopDesc,
                                    store = shopStore,
                                    bought = false
                                )
                                shopListViewModel.addToShopList(newShop)
                            } else {
                                shopListViewModel.updateItemBought(
                                    shopItem,
                                    shopTitle,
                                    shopQuant,
                                    shopDesc,
                                    shopCost,
                                    shopStore,
                                    shopCat,
                                    shopBought
                                )
                            }
                            onDialogClose()
                        }
                    }, modifier = Modifier.padding(5.dp)) {
                        Text(text = stringResource(com.ait.groceriesyou.R.string.OK_BTN))
                    }

                    Button(
                        onClick = {
                            onDialogClose()
                        }, modifier = Modifier.padding(5.dp)) {
                        Text(text = stringResource(com.ait.groceriesyou.R.string.cancel_BTN))
                    }
                }
            }
        }
    }
}

