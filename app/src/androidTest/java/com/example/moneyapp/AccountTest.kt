package com.example.moneyapp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.moneyapp.presentation.adapter.PersonAdapter
import com.example.moneyapp.presentation.adapter.ServiceAdapter
import com.example.moneyapp.presentation.ui.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.*
import kotlin.math.log


@RunWith(AndroidJUnit4::class)
@LargeTest
class AccountTest : AccountTestCase() {

    /*@Before
    fun setup() {
        launchActivity<LoginActivity>()
    }*/
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun use_account_home_fragment(){
        login()

        Thread.sleep(1000)

        //name user
        onView(withId(R.id.textFullUserName))
            .check(matches(withText(containsString("Robert Robert"))))

        Thread.sleep(1000)
        onView(withId(R.id.textNumberCard))
            .check((matches(withText(containsString("**6622")))))

        checkBalance()

        //onView(withId(R.id.textViewBalance)).check(matches(withText(containsString("$10.00"))))
    }

    fun checkBalance(){
        //balance
        val userRef = FirebaseDatabase.getInstance().reference.child("User").push()
        val query = userRef.orderByKey().equalTo("1235567950916622")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(itemSnapshot in snapshot.children){
                    var getBalance = itemSnapshot.child("balance").getValue(
                        Double::class.java
                    )!!

                    val balance = NumberFormat.getCurrencyInstance().format(getBalance)
                    println(balance)

                    Thread.sleep(1000)
                    onView(withId(R.id.textViewBalance))
                        .check(matches(withText(containsString(balance))))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                error.toException()
            }

        })
    }

    @Test
    fun use_account_cards_fragment() {
        testCardFragment()

        onView(withId(R.id.textCardViewNumber)).check(matches(withText(containsString("**** **** **** 6622"))))

        //balance

        //Thread.sleep(1000)

        onView(withId(R.id.cardViewPager)).perform(swipeLeft())

        onView(withId(R.id.scroll_block)).perform(swipeUp())

        Thread.sleep(2000)

        onView(withId(R.id.viewPagerTabs)).perform(swipeLeft())

        Thread.sleep(2000)

        onView(withId(R.id.viewPagerTabs)).perform(swipeRight())

    }

    @Test
    fun use_account_transaction_fragment(){
        testTransactionFragment()

        Thread.sleep(2000)
        //first tab
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())
        //onView(withId(R.id.viewPagerTabTransaction)).perform(swipeDown())

        Thread.sleep(2000)
        onView(withId(R.id.transaction_scroll)).perform(swipeDown())

        Thread.sleep(2000)

        onView(withId(R.id.transaction_scroll)).perform(swipeLeft())

        Thread.sleep(2000)
        //second tab
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())

        onView(withId(R.id.transaction_scroll)).perform(swipeDown())

        Thread.sleep(2000)
        onView(withId(R.id.transaction_scroll)).perform(swipeLeft())

        Thread.sleep(2000)
        //third tab
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())

        Thread.sleep(2000)
        onView(withId(R.id.transaction_scroll)).perform(swipeDown())


        Thread.sleep(2000)
        onView(withId(R.id.transaction_scroll)).perform(swipeRight())

        Thread.sleep(2000)
        onView(withId(R.id.transaction_scroll)).perform(swipeRight())
    }


    @Test
    fun use_account_profile_fragment(){
        testAccountFragment()

        onView(withId(R.id.textUserName))
            .check(matches(withText("Robert Robert")))

        onView(withId(R.id.imageShowUserNumber)).perform(click())
        Thread.sleep(500)

        onView(withId(R.id.textUserNumber)).check(matches(withText(containsString("1235 5679 5091 6622"))))

        onView(withId(R.id.editTextUserFirstName)).check(matches(withText(containsString("Robert Robert"))))

        onView(withId(R.id.editTextUserPhoneNumber)).check(matches(withText(containsString("+380-99-200-20-20"))))

        onView(withId(R.id.editTextUserEmail)).check(matches(withText(containsString("robert@gmail.com"))))

        onView(withId(R.id.editTextUserHomeAddress)).check(matches(withText(containsString("Rome"))))
    }

    @Test
    fun use_transfer_fragment(){
        login()

        Thread.sleep(1000)

        onView(withId(R.id.serviceRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ServiceAdapter.ServiceViewHolder>(0, click()))

        Thread.sleep(2000)

        use_transfer_fragment_test_case_1()

        use_transfer_fragment_test_case_2()

        use_transfer_fragment_test_case_3()

        use_transfer_fragment_test_case_4()

        use_transfer_fragment_test_case_5()

    }

    private fun use_transfer_fragment_test_case_1(){
        onView(withId(R.id.button_add_person)).perform(click())

        Thread.sleep(2000)

        onView(withText("CANCEL")).perform(click())

        Thread.sleep(2000)
    }

    private fun use_transfer_fragment_test_case_2() {
        onView(withId(R.id.button_add_person)).perform(click())

        onView(withId(R.id.edit_text_receiptent))
            .perform(replaceText("11"))

        onView(withText("OK")).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.text_user_field))
            .check(matches(withText(containsString("User's number is wrong!"))))
    }

    private fun use_transfer_fragment_test_case_3() {
        onView(withId(R.id.button_add_person)).perform(click())

        onView(withId(R.id.edit_text_receiptent))
            .perform(replaceText("1235567950916623"))

        onView(withText("OK")).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.text_user_field))
            .check(matches(withText(containsString(" 1235 5679 5091 6623  - \nWolodimyr Zelenskiy"))))

        onView(withId(R.id.editTextSum)).perform(replaceText("0"))

        onView(withId(R.id.button_send)).perform(click())

        Thread.sleep(2000)

        onView(withText("CANCEL")).perform(click())

        Thread.sleep(2000)
    }

    private fun use_transfer_fragment_test_case_4() {
        onView(withId(R.id.personRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<PersonAdapter.PersonViewHolder>(2, click()))
        Thread.sleep(2000)

        onView(withId(R.id.text_user_field))
            .check(matches(withText(containsString(" 1234 5678 5090 6620  - \n" +
                    "Hero Hero"))))
        onView(withId(R.id.editTextSum)).perform(replaceText("10000"))

        onView(withId(R.id.button_send)).perform(click())

        onView(withText("CANCEL")).perform(click())

        Thread.sleep(2000)
    }

    private fun use_transfer_fragment_test_case_5() {
        onView(withId(R.id.editTextSum)).perform(replaceText("5.6"))
        onView(withId(R.id.button_send)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.textDetailName))
            .check(matches(withText(containsString("Hero Hero"))))

        onView(withId(R.id.texDetailNumberCard))
            .check(matches(withText(containsString("1234 5678 5090 6620"))))


        onView(withId(R.id.textDetailSum))
            .check(matches(withText(containsString(NumberFormat.getCurrencyInstance(Locale.US).format(5.6).toString()))))

        Thread.sleep(1000)

        onView(withId(R.id.success_block)).perform(swipeUp())

        onView(withId(R.id.buttonDone)).perform(click())

        Thread.sleep(1000)

        checkBalance()

    }

}