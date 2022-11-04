package com.example.moneyapp.domain.use_cases

import com.example.moneyapp.domain.entities.Operation

open class SingleTransactionFactory(operation: Operation) {

    init{
        OPERATION = operation
    }

    companion object{
        lateinit var OPERATION: Operation
    }
}