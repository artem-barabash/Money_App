package com.example.moneyapp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
import kotlin.math.log


@RunWith(AndroidJUnit4::class)
@LargeTest
class AccountTest : AccountTestCase() {

    @Before
    fun setup() {
        launchActivity<LoginActivity>()
    }

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

        //onView(withId(R.id.textViewBalance)).check(matches(withText(containsString("$10.00"))))
    }

    @Test
    fun use_account_cards_fragment() {
        testCardFragment()

        onView(withId(R.id.textCardViewNumber)).check(matches(withText(containsString("**** **** **** 6622"))))

        //balance

        //Thread.sleep(1000)

        onView(withId(R.id.cardViewPager)).perform(swipeLeft())

        onView(withId(R.id.scroll_block)).perform(swipeUp())

        Thread.sleep(1000)

        onView(withId(R.id.viewPagerTabs)).perform(swipeLeft())

        Thread.sleep(1000)

        onView(withId(R.id.viewPagerTabs)).perform(swipeRight())

    }

    @Test
    fun use_account_transaction_fragment(){
        testTransactionFragment()

        //first tab
        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeUp())

        Thread.sleep(1000)

        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeDown())

        Thread.sleep(1000)

        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeLeft())

        //second tab
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())

        onView(withId(R.id.transaction_scroll)).perform(swipeDown())

        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeLeft())

        //third tab
        onView(withId(R.id.transaction_scroll)).perform(swipeUp())

        onView(withId(R.id.transaction_scroll)).perform(swipeDown())


        Thread.sleep(500)
        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeRight())

        Thread.sleep(500)
        onView(withId(R.id.viewPagerTabTransaction)).perform(swipeRight())
    }


}