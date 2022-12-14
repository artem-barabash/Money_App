package com.example.moneyapp.domain.use_cases

import android.app.Application
import com.example.moneyapp.data.room.AppDatabase

class UserDataApplication: Application() {
    val database: AppDatabase by lazy{ AppDatabase.getDatabase(this) }
}