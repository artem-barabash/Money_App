package com.example.moneyapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import getOrAwaitValue
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegistrationViewModelTest:TestCase(){
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel:RegistrationViewModel

    @Before
    public override fun setUp() {
        viewModel = RegistrationViewModel()
    }

    @After
    fun close(){
        viewModel._status.value = ""
    }

    @Test
    fun check_fields_by_empty_case_1(){
        val userExample = User(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0.0
        )

        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = userExample,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = userExample,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("Fields can not be empty!", viewModel.status.value)
    }

    @Test
    fun check_fields_by_empty_case_2(){
        val user = User(
            "Ivan",
            "",
            "+380-99-000-00-00",
            "ivan70@gmail.com",
            "09.9.1989",
            "MALE",
            "Kharkiv",
            0.0
        )


        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = user,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = user,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("Fields can not be empty!", viewModel.status.value)
    }

    @Test
    fun check_fields_by_empty_case_3(){

        val user = User(
            "Ivan",
            "Ivanenko",
            "+380-99-000-00-00",
            "",
            "09.9.1989",
            "MALE",
            "Kharkiv",
            0.0
        )

        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = user,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = user,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("Fields can not be empty!", viewModel.status.value)
    }

    @Test
    fun check_field_email(){

        val user = User(
            "Ivan",
            "Ivanenko",
            "+380-99-000-00-00",
            "ivan@mail.ru",
            "09.9.1989",
            "MALE",
            "Kharkiv",
            0.0
        )

        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = user,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = user,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("The user information is not correct!", viewModel.status.value)
    }

    @Test
    fun check_field_phone(){

        val user = User(
            "Ivan",
            "Ivanenko",
            "0990000000",
            "ivan@gmail.com",
            "09.9.1989",
            "MALE",
            "Kharkiv",
            0.0
        )

        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = user,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = user,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("The user information is not correct!", viewModel.status.value)
    }

    @Test
    fun check_field_age(){

        val user = User(
            "Ivan",
            "Ivanenko",
            "+380-99-000-00-00",
            "ivan@gmail.com",
            "2009-09-09",
            "MALE",
            "Kharkiv",
            0.0
        )

        assertEquals(false,
            viewModel.checkUserRegistrationData(
                user = user,
                password = "12",
                repeatPassword = "12"
            )
        )


        viewModel.checkUserRegistrationData(
            user = user,
            password = "12",
            repeatPassword = "12"
        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("The client's age cannot be less than 16 years!", viewModel.status.value)
    }

    @Test
    fun check_field_compare_password_failure(){

        assertEquals(false,
            viewModel.comparePassword(
                "Story-9898",
                "Story-9090"

            )
        )


        viewModel.comparePassword(
            "Story-9898",
            "Story-9090"

        )

        viewModel.status.getOrAwaitValue {  }

        assertEquals("Passwords do not match!", viewModel.status.value)
    }

    @Test
    fun check_field_compare_password(){

        assertEquals(true,
            viewModel.comparePassword(
                "Story-9898",
                "Story-9898"

            )
        )
    }

    @Test
    fun check_user_to_success(){
        val user = User(
            "Ivan",
            "Ivanenko",
            "+380-99-000-00-00",
            "ivan@gmail.com",
            "1909-09-09",
            "MALE",
            "Kharkiv",
            0.0
        )


        assertEquals(
            true,
            viewModel.checkUserRegistrationData(user, "1", "1")
        )

    }
}