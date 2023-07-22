package com.ait.groceriesyou.ui.screen.PromptSpinners

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ait.groceriesyou.R
import com.ait.groceriesyou.ui.data.ShopList.ItemStore
import com.ait.groceriesyou.ui.data.ShopList.s

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSpinner(
    selectedStore: (ItemStore) -> Unit,
    oldStore: ItemStore,
    listStores: List<String>,
    onDialogClose: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var storeInput by rememberSaveable { mutableStateOf(oldStore.s) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = storeInput,
            readOnly = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.storeLabelSpinner)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        // filter options based on text field value
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listStores.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        storeInput = selectionOption
                        selectedStore(ItemStore.valueOf(selectionOption))
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )

            }
        }
    }
}