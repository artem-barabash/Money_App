package com.example.moneyapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.moneyapp.data.room.AppDatabase
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import junit.framework.TestCase
import org.junit.Assert.*

import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

import androidx.test.ext.junit.runners.AndroidJUnit4
import getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.io.IOException

@RunWith(AndroidJUnit4::class)
//@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest :TestCase() {


    private lateinit var userAccount: UserAccount
    private lateinit var viewModel: HomeViewModel


    @Mock
    private lateinit var db: AppDatabase

    private  var userDao = mock(OperationDao::class.java)

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()


    @Before
    override fun setUp(){

        super.setUp()
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



    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun check_user_balance()= runBlocking{


        viewModel =  HomeViewModel(userAccount, userDao)

        val b =  NumberFormat.getCurrencyInstance(Locale("en", "US")).format(userAccount.user.balance)

        val value = viewModel.balance


        //assertEquals(2, 2)

        assertEquals(b, value)
        //assertThat(value, (not(nullValue())))
    }
}