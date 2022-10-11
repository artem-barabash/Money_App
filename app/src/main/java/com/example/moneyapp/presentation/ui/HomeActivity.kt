package com.example.moneyapp.presentation.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moneyapp.databinding.ActivityHomeBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.BALANCE
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.BIRTHDAY
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.EMAIL
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.FIRST_NAME
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.GENDER
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.HOME_ADDRESS
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.IMAGE_LINK
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.LAST_NAME
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.NUMBER_KEY
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.PASSWORD
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.PHONE
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.TEMP_USER_DATA
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(TEMP_USER_DATA, MODE_PRIVATE)

        val nBalance: String? = sharedPreferences.getString(BALANCE, "")

        val balance: Double = if(nBalance != null && nBalance != "0"){
            nBalance.toDouble()
        }else{
            0.0
        }

        var user = User(
            sharedPreferences.getString(FIRST_NAME, "").toString(),
            sharedPreferences.getString(LAST_NAME, "").toString(),
            sharedPreferences.getString(PHONE, "").toString(),
            sharedPreferences.getString(EMAIL, "").toString(),
            sharedPreferences.getString(BIRTHDAY, "").toString(),
            sharedPreferences.getString(GENDER, "").toString(),
            sharedPreferences.getString(HOME_ADDRESS, "").toString(), balance)

        val numberKey = sharedPreferences.getString(NUMBER_KEY, "").toString()
        val password = sharedPreferences.getString(PASSWORD, "").toString()
        var imageLink = sharedPreferences.getString(IMAGE_LINK, "").toString()

        val list = ArrayList<Operation>()

        userAccount = UserAccount(numberKey, password, imageLink, user, list)

        binding.textFullUserName.text = "${user.firstName} ${user.lastName}"
       ///println(userAccount)


    }

    companion object{
        lateinit var userAccount: UserAccount
    }

}

