package com.ait.groceriesyou.ui.data.ShopList

import android.content.ClipData.Item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "shoptable")
data class ShopItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    @ColumnInfo(name = "ownerid") var ownerid : Long,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "quantity") var quantity:Int = 1,
    @ColumnInfo(name = "disc") var desc: String = "",
    @ColumnInfo(name = "cost") var cost: String = "",
    @ColumnInfo(name = "store") var store: ItemStore = ItemStore.All,
    @ColumnInfo(name = "cat") var cat:ItemCategory = ItemCategory.Misc,
    @ColumnInfo(name = "bought") var bought: Boolean = false,
//    @ColumnInfo(name = "date") var date: LocalDate = LocalDate.parse("0000-01-01"),
//    @ColumnInfo(name = "color") var color: Int = Color.parseColor("#00ff0000")
)

val StoreList = ItemStore.values().map { it.s }
val CategoryList = ItemCategory.values().map { it.s }


enum class ItemCategory {
    Auto, Electronics, Food, Home, Misc;

    fun getIcon(): ImageVector {
        // The this is the value of this enum object
        var icon = Icons.Outlined.ShoppingCart
        if (this == Auto) {
            icon = Icons.Outlined.DirectionsCar
        } else if (this == Electronics) {
            icon = Icons.Outlined.Power
        } else if (this == Food) {
            icon = Icons.Outlined.Restaurant
        }  else if (this == Home) {
            icon = Icons.Outlined.House
        }else if (this == Misc) {
            icon = Icons.Outlined.ShoppingCart
        }

        return icon
    }

    fun getIconColor(): Color {
        // The this is the value of this enum object
        var color = Color.White
        if (this == Auto) {
            color = Color(0xFFE34D4A)
        } else if (this == Electronics) {
            color = Color(0xFF3A4A71)
        } else if (this == Food) {
            color = Color(0xFF68BB81)
        }  else if (this == Home) {
            color = Color(0xFFF4A871)
        }else if (this == Misc) {
            color = Color(0xFFEFDEBC)
        }

        return color
    }

}

val ItemCategory.s get() = when(this) {
    ItemCategory.Auto -> "Auto"
    ItemCategory.Electronics -> "Electronics"
    ItemCategory.Food -> "Food"
    ItemCategory.Home -> "Home"
    ItemCategory.Misc -> "Misc"
}

enum class ItemStore {
    All, Aldi, Auchan, Lidl, Spar, Other;

//    fun getIcon(): Int {
//        // The this is the value of this enum object
//        return if (this == NORMAL) R.drawable.normal else R.drawable.important
//    }


}


//fun main(args : Array<String>) {
//    println(ItemStore.valueOf("All") == ItemStore.All)
//    println(ItemStore.valueOf("Aldi") == ItemStore.Aldi)
//    println(ItemStore.valueOf("Auchan") == ItemStore.Auchan)
//    println(ItemStore.valueOf("Lidl") == ItemStore.Lidl)
//    println(ItemStore.valueOf("Spar") == ItemStore.Spar)
//    println(ItemStore.valueOf("Other") == ItemStore.Other)
//
//
//    println(ItemStore.values().toList())
//}

val ItemStore.s get() = when(this) {
    ItemStore.All -> "All"
    ItemStore.Aldi -> "Aldi"
    ItemStore.Auchan -> "Auchan"
    ItemStore.Lidl -> "Lidl"
    ItemStore.Spar -> "Spar"
    ItemStore.Other -> "Other"
}







