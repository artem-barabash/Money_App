package com.example.moneyapp.domain.entities

data class Operation(
    val send: String,
    val receive: String,
    val time: String,
    val sum: Double
    )