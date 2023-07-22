package com.ait.groceriesyou.ui.screen

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.ColumnInfo
import androidx.room.Dao
import com.ait.groceriesyou.MainApplication
import com.ait.groceriesyou.ui.data.ShopList.ItemCategory
import com.ait.groceriesyou.ui.data.ShopList.ItemStore
import com.ait.groceriesyou.ui.data.ShopList.ShopDAO
import com.ait.groceriesyou.ui.data.ShopList.ShopItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.cos

class ShopListViewModel(
    private val shopDAO: ShopDAO,
) : ViewModel() {


    val sortableBy = arrayOf<String>(
        "Date Added",
        "Name",
        "Category",
        "Store",
        "Price")

    fun getSortables(): Array<String> {
        return sortableBy
    }

    fun getAllItems(): Flow<List<ShopItem>> {
        return shopDAO.getAllShopItems()
    }

    fun getAllItemsByTitle(): Flow<List<ShopItem>> {
        return shopDAO.getShopItemsByTitle()
    }

    fun getAllItemsByPrice(): Flow<List<ShopItem>> {
        return shopDAO.getShopItemsByPrice()
    }

    fun getUsedStores(): Flow<List<ItemStore>> {
        return shopDAO.getUsedStores()
    }

    fun getUsedCategories(): Flow<List<ItemCategory>> {
        return shopDAO.getUsedCategories()
    }

    fun getItemsByStore(store: ItemStore): Flow<List<ShopItem>> {
        return shopDAO.loadItemsByStore(store)
    }

    fun getItemsByCategory(category: ItemCategory): Flow<List<ShopItem>> {
        return shopDAO.loadItemsByCategory(category)
    }

    fun getCostByCategory(category: ItemCategory): Double {
        return getCostCategoryAsyncTask(shopDAO, category).execute().get()
    }

    fun getTotalCost(): Double {
        return totalCostAsyncTask(shopDAO).execute().get()
    }

    fun getBoughtCost(): Double {
        return getCostBoughtAsyncTask(shopDAO).execute().get()
    }

    fun addToShopList(shopItem: ShopItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopDAO.insert(shopItem)
            }
        }
    }

    fun removeItem(shopItem: ShopItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopDAO.delete(shopItem)
            }
        }
    }

    fun clearBoughtItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopDAO.deleteBoughtShopItems()
            }
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopDAO.deleteAllShopItems()
            }
        }
    }

    fun updateItemBought(
        shopItem: ShopItem,
        title: String = shopItem.title,
        quantity:Int = shopItem.quantity,
        desc: String = shopItem.desc,
        cost: String = shopItem.cost,
        store: ItemStore = shopItem.store,
        cat: ItemCategory = shopItem.cat,
       bought: Boolean = shopItem.bought,
        )
    {
        // copy function causes recomposition
        val newShopItem = shopItem.copy()
        newShopItem.title = title
        newShopItem.quantity = quantity
        newShopItem.cost = cost
        newShopItem.desc = desc
        newShopItem.store = store
        newShopItem.cat = cat
        newShopItem.bought = bought


        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopDAO.update(newShopItem)
            }
        }
    }

    private class totalCostAsyncTask(private val mDao: ShopDAO) : AsyncTask<Void, Void, Double>() {
        protected override fun doInBackground(vararg voids: Void): Double {
            return mDao.getTotalCost()
        }

    }

    private class getCostCategoryAsyncTask(private val mDao: ShopDAO,private val category: ItemCategory) : AsyncTask<Void, Void, Double>() {
        protected override fun doInBackground(vararg voids: Void): Double {
            return mDao.getCostByCategory(category)
        }

    }

    private class getCostBoughtAsyncTask(private val mDao: ShopDAO) : AsyncTask<Void, Void, Double>() {
        protected override fun doInBackground(vararg voids: Void): Double {
            return mDao.getBoughtCost()
        }

    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                ShopListViewModel(
                    shopDAO = application.database.shopDAO(),
                    )
            }
        }
    }
}



