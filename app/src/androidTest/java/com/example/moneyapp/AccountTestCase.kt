package com.example.moneyapp

import android.text.TextUtils.replace
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.moneyapp.presentation.ui.LoginActivity
import org.junit.Before
import org.junit.Test

open class AccountTestCase {

    open fun login(){
        onView(withId(R.id.editTextEmail)).perform(replaceText("robert@gmail.com"))
        onView(withId(R.id.editTextPassword)).perform(replaceText("Lemish@1234"))

        onView(withId(R.id.buttonLogin)).perform(click())

        Thread.sleep(1000)

    }

    open fun testCardFragment(){
        login()
        Thread.sleep(500)
        onView(withId(R.id.itemCard)).perform(click())
        Thread.sleep(500)
    }

    open fun testTransactionFragment(){
        login()
        Thread.sleep(1000)
        onView(withId(R.id.itemTransactions)).perform(click())
        Thread.sleep(2000)
    }

    open fun testAccountFragment(){
        login()
        Thread.sleep(1000)
        onView(withId(R.id.itemProfile)).perform(click())
        Thread.sleep(2000)

    }
}