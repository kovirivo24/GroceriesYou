package com.ait.groceriesyou.ui.data

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ait.groceriesyou.ui.data.ShopList.ShopDAO
import com.ait.groceriesyou.ui.data.ShopList.ShopItem
import com.ait.groceriesyou.ui.screen.ShopListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@Database(entities = [ShopItem::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopDAO(): ShopDAO


    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "main_database")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}