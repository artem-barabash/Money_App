package com.example.moneyapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.moneyapp.presentation.ui.RegistrationActivity
import java.time.LocalDate

open class BaseTestCase {

    open fun fullFields(){
        onView(withId(R.id.textFirstName)).perform(replaceText("Nick"))

        onView(withId(R.id.textLastName)).perform(replaceText("Nicon"))

        val localDate = LocalDate.of(2000, 1, 1)

        //onView(withId(R.id.textViewDate)).perform(replaceText(""))

        RegistrationActivity.dateBirthday = localDate

        //onView(withId(R.id.textViewDate)).perform(replaceText(""))

        //onView(withId(R.id.textViewDate)).perform(replaceText("${localDate.dayOfMonth}.${localDate.monthValue}.${localDate.year}"))

        onView(withId(R.id.textHomeAddress)).perform(replaceText("Kyiv"))

        onView(withId(R.id.textPhoneNumber)).perform(replaceText("+380-99-900-99-99"))

        onView(withId(R.id.textEmailAddress)).perform(replaceText("nick@gmail.com"))

        onView(withId(R.id.textPassword)).perform(replaceText("Noise-9090"))

        onView(withId(R.id.textRepeatPassword)).perform(replaceText("Noise-9090"))
    }
}