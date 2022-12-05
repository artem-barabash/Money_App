package com.example.moneyapp.presentation.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.moneyapp.data.room.AppDatabase
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import junit.framework.TestCase

import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import getOrAwaitValue
import org.junit.*

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest:TestCase() {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var userAccount: UserAccount
    private lateinit var viewModel: HomeViewModel

    private lateinit var db: AppDatabase
    private lateinit var userDao: OperationDao


    @Before
    public override fun setUp(){
        userAccount = UserAccount(
            "1235567950916621",
            "",
            "",
            User(
                "Gg",
                "Hh",
                "+380-99-600-99-99",
                "ivan90@gmail.com",
                "21.6.2022",
                "MALE", "Rome",
                49.39
            ),
            ArrayList<Operation>()
        )

        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = db.operationDao()


        viewModel = HomeViewModel(userAccount, userDao)

        //viewModel.viewModelScope.launch {  }
        //viewModel.initializeUserAccount()


        UserAccountFactory(userAccount)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun check_full_user_name() {
        viewModel.user.getOrAwaitValue {  }

        assertEquals("Gg Hh",viewModel.getFullUserName())
    }

    @Test
    fun check_user_balance() {

        println("balance = " + viewModel.balance.value)
        val b = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                .format(userAccount.user.balance)

        viewModel.balance.getOrAwaitValue {  }

       assertEquals(b, viewModel.balance.value)
    }


    @Test
    fun check_number_recipient(){
        /*val person = "1235 5679 5091 6622 - \nRobert Robert"


        viewModel.recipient.getOrAwaitValue {}
        viewModel.checkNumberInFirebase("1235567950916622")
        assertEquals(person, viewModel.recipient.value)*/
    }

    @Test
    fun check_show_card(){
        val res = viewModel.showCardNumber("1235567950916622")

        assertEquals("1235 5679 5091 6622", res!!.trim())
    }


}