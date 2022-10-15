package com.example.moneyapp.domain.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Service(
    @DrawableRes val imageId: Int,
    @StringRes val nameId: Int
)