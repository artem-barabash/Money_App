package com.example.moneyapp.domain.use_cases

import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User

data class UserAccount(
    val number: String,
    val password: String,
    var image: String,
    val user: User,
    var operations: List<Operation>
)