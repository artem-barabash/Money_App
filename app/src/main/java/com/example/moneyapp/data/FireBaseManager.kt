package com.example.moneyapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.ui.LoginActivity
import com.example.moneyapp.presentation.ui.LoginActivity.Companion.TEMP_USER_DATA
import com.google.firebase.database.*


class FireBaseManager {

    private val databaseReference: DatabaseReference= FirebaseDatabase.getInstance().reference

    private val num:Int = 4

    fun addUser(user: User, password: String){
        val usersRef: DatabaseReference = databaseReference.child("User")

        val query: Query = usersRef.orderByKey().limitToLast(1)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            var lastNumber: String? = null
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                //асинхроно перебираем список, ключ можно узнать через getValue
                lastNumber = snapshot.value.toString()
                //добавляем 1 к номеру счета предыдущего пользоавтеля, и получаем номер счета нового пользователя
                lastNumber = lastNumber!!.substring(1, num * num + 1)
                println("lastNumber$lastNumber")

                userNumber = generateCode(lastNumber!!)
                //добавлем в FB
                usersRef.child(userNumber).setValue(user)


                val list = ArrayList<Operation>()
                val userAccount = UserAccount(userNumber, password,"", user, list)
                val pdf = PDFFileManager()
                pdf.createPdf(userAccount)

            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    private fun generateCode(lastNumber: String): String {
        val arrNum = IntArray(num)

        for (i in arrNum.indices) arrNum[i] = lastNumber.substring(i * num, (i + 1) * num).toInt()

        //TODO доработать функцию для коректной выдачи номера

        //TODO доработать функцию для коректной выдачи номера
        for (i in arrNum.indices.reversed()) {
            if ((arrNum[i] + 1) % 10000 != 0) {
                arrNum[i] += 1
                break
            }
        }

        val sb = StringBuilder()

        for (i in arrNum) sb.append(i)

        return sb.toString()
    }

    companion object{
        var userNumber: String = ""
    }


}