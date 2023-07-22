package com.ait.groceriesyou.ui.data

import androidx.room.Embedded
import androidx.room.Relation
import com.ait.groceriesyou.ui.data.ShopList.ShopItem

data class ListAndShopItem(
    @Embedded val listItem : UserListItem,
    @Relation(
        parentColumn = "listID",
        entityColumn = "shopID"
    )
    val shopItem: ShopItem
)
