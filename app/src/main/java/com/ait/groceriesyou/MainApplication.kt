package com.ait.groceriesyou

import android.app.Application
import com.ait.groceriesyou.ui.data.AppDatabase

class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

}