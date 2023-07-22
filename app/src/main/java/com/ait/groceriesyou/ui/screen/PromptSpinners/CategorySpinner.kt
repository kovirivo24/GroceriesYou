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
import com.ait.groceriesyou.ui.data.ShopList.ItemCategory
import com.ait.groceriesyou.ui.data.ShopList.s

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySpinner(
    selectedCat: (ItemCategory) -> Unit,
    oldCat: ItemCategory,
    listCat: List<String>,
    onDialogClose: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var catInput by rememberSaveable { mutableStateOf(oldCat.s) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = catInput,
            readOnly = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.catLabelSpinner)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listCat.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        catInput = selectionOption
                        selectedCat(ItemCategory.valueOf(selectionOption))
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            ItemCategory.valueOf(selectionOption).getIcon(),
                            contentDescription = null
                        )
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }

        }
    }
}