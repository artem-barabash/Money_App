package com.example.moneyapp.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ActivityScenario.launch
import com.example.moneyapp.R
import com.example.moneyapp.databinding.ActivityHomeBinding
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.example.moneyapp.domain.use_cases.UserDataApplication
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
import com.example.moneyapp.presentation.ui.fragments.*
import com.example.moneyapp.presentation.viewmodel.HomeViewModel
import com.example.moneyapp.presentation.viewmodel.factory.HomeViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlin.concurrent.thread


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var page = 0


    private val sharedViewModel: HomeViewModel by viewModels{
        HomeViewModelFactory(
            ACCOUNT,
            (this.application as UserDataApplication).database.operationDao()
        )
    }


    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

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
            sharedPreferences.getString(HOME_ADDRESS, "").toString(),
            balance
        )



        val numberKey = sharedPreferences.getString(NUMBER_KEY, "").toString()
        val password = sharedPreferences.getString(PASSWORD, "").toString()
        var imageLink = sharedPreferences.getString(IMAGE_LINK, "").toString()

        val list = ArrayList<Operation>()
        val userAccount = UserAccount(numberKey, password, imageLink, user, list)
        UserAccountFactory(userAccount)


        changePage(page)


        binding.bottomNavigationBar.setOnItemSelectedListener {


            when (it.itemId){
                R.id.itemHome -> {page = 0}
                R.id.itemCard -> {page = 1}
                R.id.itemTransactions -> {page = 2}
                R.id.itemProfile -> {page = 3}
            }

            changePage(page)


            //replaceFragment(selectedFragment)

            return@setOnItemSelectedListener true
        }

        binding.swiperefresh.setOnRefreshListener {
            val i = Intent(this@HomeActivity, HomeActivity::class.java)
            finish();
            overridePendingTransition(1, 1);
            startActivity(i);
        }
    }


    private fun changePage(changedPage:Int){
        when(changedPage){
            0 -> replaceFragment(HomeFragment())
            1 -> replaceFragment(CardFragment())
            2 -> replaceFragment(TransactionListFragment())
            3 -> replaceFragment(AccountFragment())
        }
    }

    private fun replaceFragment(selected: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_layout, selected)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.bottomNavigationBar.selectedItemId =  R.id.itemHome
    }


    companion object {
        const val KEY_PAGE = "key_page"

        const val KEY_PAGE_INDEX_CACHE = "key_page_index_cache"

        const val KEY_PAGE_INDEX = "key_page_index"
    }

}

