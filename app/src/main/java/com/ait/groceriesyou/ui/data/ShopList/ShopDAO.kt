package com.ait.groceriesyou.ui.data.ShopList

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ait.groceriesyou.ui.data.ListAndShopItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDAO {
    @Query("SELECT * from shoptable")
    fun getAllShopItems(): Flow<List<ShopItem>>

    @Query("SELECT * FROM shoptable ORDER BY title ASC")
    fun getShopItemsByTitle(): Flow<List<ShopItem>>

    @Query("SELECT * FROM shoptable ORDER BY CAST(cost as INTEGER) * quantity")
    fun getShopItemsByPrice(): Flow<List<ShopItem>>

    @Query("SELECT SUM(cost * quantity) from shoptable WHERE cat = :category")
    fun getCostByCategory(category: ItemCategory): Double

    @Query("SELECT SUM(cost * quantity) from shoptable")
    fun getTotalCost(): Double

    @Query("SELECT SUM(cost) from shoptable WHERE bought = true")
    fun getBoughtCost(): Double

    @Query("SELECT * from shoptable WHERE store = :store")
    fun loadItemsByStore(store : ItemStore): Flow<List<ShopItem>>

    @Query("SELECT DISTINCT store from shoptable ORDER BY store ASC")
    fun getUsedStores(): Flow<List<ItemStore>>

    @Query("SELECT DISTINCT cat from shoptable ORDER BY cat ASC")
    fun getUsedCategories(): Flow<List<ItemCategory>>

    @Query("SELECT * from shoptable WHERE cat = :category")
    fun loadItemsByCategory(category : ItemCategory): Flow<List<ShopItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shopItem: ShopItem)

    @Update
    suspend fun update(shopItem: ShopItem)

    @Delete
    suspend fun delete(shopItem: ShopItem)

    @Query("DELETE from shoptable")
    suspend fun deleteAllShopItems()

    @Query("DELETE from shoptable WHERE bought = true")
    suspend fun deleteBoughtShopItems()

}