package com.example.moneyapp.domain.use_cases

open class UserAccountFactory(val userAccount: UserAccount) {

  init {
      ACCOUNT = userAccount
  }

    companion object{

        lateinit var ACCOUNT : UserAccount
    }
}