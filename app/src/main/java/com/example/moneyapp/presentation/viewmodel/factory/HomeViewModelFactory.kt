package com.example.moneyapp.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.viewmodel.HomeViewModel

class HomeViewModelFactory(private val userAccount: UserAccount, private val operationDao: OperationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(userAccount, operationDao) as T
        }
         throw IllegalArgumentException("Unknown ViewModel class")
    }

}