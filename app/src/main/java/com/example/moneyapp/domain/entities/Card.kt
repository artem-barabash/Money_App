package com.example.moneyapp.domain.entities

import androidx.annotation.DrawableRes

data class Card(
    val type:String,
    val number:String,
    val date: String,
    val balance: Double
)
