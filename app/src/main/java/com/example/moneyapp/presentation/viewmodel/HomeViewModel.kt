package com.example.moneyapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moneyapp.domain.use_cases.UserAccount
import java.text.NumberFormat
import java.util.*

class HomeViewModel(userAccount: UserAccount): ViewModel() {

    private val _user = MutableLiveData<UserAccount?>()
    val user: LiveData<UserAccount?> = _user

    private val _balance = MutableLiveData(0.0)
    val balance: LiveData<String> = Transformations.map(_balance){
        NumberFormat.getCurrencyInstance(Locale("en", "US")).format(it)
    }

    init {
        _user.value = userAccount

        _balance.value = user.value?.user?.balance
    }

    fun getFullUserName():String{
        return (user.value?.user?.firstName ?: "") + " " +(user.value?.user?.lastName ?: "")
    }

}