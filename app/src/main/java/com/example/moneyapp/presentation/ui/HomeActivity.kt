package com.example.moneyapp.presentation.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.moneyapp.R
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.presentation.ui.fragments.CardFragment
import com.example.moneyapp.presentation.ui.fragments.HomeFragment
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory

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

        val userAccount = UserAccount(numberKey, password, imageLink, user, list)
        UserAccountFactory(userAccount)

        replaceFragment(HomeFragment())

        binding.bottomNavigationBar.setOnItemSelectedListener {
            var selectedFragment : Fragment = HomeFragment()

            when (it.itemId){

                R.id.itemHome -> {selectedFragment = HomeFragment()}
                R.id.itemCard -> {selectedFragment = CardFragment()}
                R.id.itemTransactions -> {}
                R.id.itemProfile -> {}
            }

            replaceFragment(selectedFragment)

            return@setOnItemSelectedListener true
        }
    }

    private fun replaceFragment(selectedFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_layout, selectedFragment)
        transaction.commit()
    }

}

