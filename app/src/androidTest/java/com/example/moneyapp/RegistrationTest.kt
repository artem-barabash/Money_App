package com.example.moneyapp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneyapp.presentation.ui.LoginActivity
import com.example.moneyapp.presentation.ui.RegistrationActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class RegistrationTest : BaseTestCase() {

    @Before
    fun setup(){
        launchActivity<LoginActivity>()

        onView(withId(R.id.buttonRegistration)).perform(click())
    }

    @Test
    fun empty_fields(){
        onView(withId(R.id.registerButton)).perform(click())

        //Thread.sleep(1000)

        onView(withText("Fields can not be empty!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_to_correct_first_name(){
        fullFields()

        onView(withId(R.id.textFirstName)).perform(replaceText("nick"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The user information is not correct!")).check(matches(isDisplayed()))
    }


    @Test
    fun check_data_user_to_correct_last_name(){
        fullFields()

        onView(withId(R.id.textLastName)).perform(replaceText("nicon"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The user information is not correct!")).check(matches(isDisplayed()))
    }

    @Test
        fun check_data_user_to_correct_home_address(){
        fullFields()

        onView(withId(R.id.textHomeAddress)).perform(replaceText("Lyubotin"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The user information is not correct!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_to_correct_phone(){
        fullFields()

        onView(withId(R.id.textPhoneNumber)).perform(replaceText("099-900-99-99"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The user information is not correct!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_to_correct_email(){
        fullFields()

        onView(withId(R.id.textEmailAddress)).perform(replaceText("nick@gmail"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The user information is not correct!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_to_correct_age(){
        fullFields()

        RegistrationActivity.dateBirthday = LocalDate.now()

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("The client's age cannot be less than 16 years!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_to_repeat_password(){
        fullFields()

        onView(withId(R.id.textRepeatPassword)).perform(replaceText("111"))

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        onView(withText("Passwords do not match!")).check(matches(isDisplayed()))
    }

    @Test
    fun check_data_user_correctly(){
        fullFields()

        onView(withId(R.id.registerButton)).perform(click())
    }

}