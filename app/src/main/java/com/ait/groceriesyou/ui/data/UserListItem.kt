package com.ait.groceriesyou.ui.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UserListItem(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    @ColumnInfo(name = "title") var title:String,
)
