package com.example.moneyapp

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.moneyapp.presentation.ui.HomeActivity
import com.example.moneyapp.presentation.ui.LoginActivity
import org.hamcrest.CoreMatchers.containsString

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @Before
    fun setup() {
        launchActivity<LoginActivity>()
    }

    @Test
    fun entered_in_account_empty_fields(){
        onView(withId(R.id.buttonLogin)).perform(click())

        onView(withText("Fields can not be empty!")).check(matches(isDisplayed()))
    }

    @Test
    fun entered_in_account_no_correctly_data(){
        onView(withId(R.id.editTextEmail)).perform(replaceText("fox@gmail.com"))

        onView(withId(R.id.editTextPassword)).perform(replaceText("abcd"))

        onView(withId(R.id.buttonLogin)).perform(click())

        Thread.sleep(1000)

        onView(withText("Login or password is not correctly!")).check(matches(isDisplayed()))
    }

    @Test
    fun entered_in_account(){
        onView(withId(R.id.editTextEmail)).perform(replaceText("fox@gmail.com"))

        onView(withId(R.id.editTextPassword)).perform(replaceText("Fix@1234"))

        onView(withId(R.id.buttonLogin)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.textFullUserName))
            .check(matches(withText(containsString("Fox Fox"))))
        //
        onView(withId(R.id.textViewBalance))
            .check(matches(withText(containsString("$23.47"))))
    }

    @Test
    fun entered_in_account_and_back_to_login(){
        onView(withId(R.id.editTextEmail)).perform(replaceText("fox@gmail.com"))

        onView(withId(R.id.editTextPassword)).perform(replaceText("Fix@1234"))

        onView(withId(R.id.buttonLogin)).perform(click())

        Thread.sleep(1000)

        onView(isRoot()).perform(pressBack())
        //проверяем на пустое поля после выхода из акаунта
        onView(withId(R.id.editTextEmail)).
                check(matches(withText(containsString(""))))

        onView(withId(R.id.editTextPassword)).
        check(matches(withText(containsString(""))))
    }

}