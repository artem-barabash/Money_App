package com.example.moneyapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.Person
import com.example.moneyapp.domain.entities.User
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.viewmodel.HomeViewModel.Companion.RECEIVE
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class FireBaseManager {



    private var databaseReference: DatabaseReference= FirebaseDatabase.getInstance().reference

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
                pdf.createUserPdf(userAccount)

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

    fun retrieveOperations(query: String, number: String, operationDao: OperationDao){
        val operationRef = databaseReference.child("Operation")


        operationRef.orderByChild(query).equalTo(number)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot!!.exists()) {
                        val list = java.util.ArrayList<Operation>()

                        for (h in snapshot.children) {

                            val sum: Any? = h.child("sum").value

                            val nSum: Double = if (sum != null && sum.toString() != "0") {
                                sum.toString().toDouble()
                            } else 0.0


                            val operation = Operation(
                                COUNT_OPERATION,
                                h.child("send").getValue(String::class.java)!!,
                                h.child("receive").getValue(String::class.java)!!,
                                h.child("time").getValue(String::class.java)!!,
                                nSum
                            )

                            list.add(operation)
                            COUNT_OPERATION++
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            operationDao.insertAll(list)
                            getPersonsFromBase(operationDao, list, query)
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException()
                }
            })
    }

    private fun getPersonsFromBase(operationDao: OperationDao, list: ArrayList<Operation>, queryDB: String) {
        var operationHashSet = HashSet<String>()

       if(queryDB == RECEIVE){
           for (i in list){
               operationHashSet.add(i.send)
           }
       }else{
           for (i in list){
               operationHashSet.add(i.receive)
           }
       }



        val usersRef: DatabaseReference = databaseReference.child("User")

        var iterator = operationHashSet.iterator()





        val personList = ArrayList<Person>()
        while (iterator.hasNext()) {
            usersRef.orderByKey().equalTo(iterator.next())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(itemSnapshot in snapshot.children){
                                val numberRecipient = itemSnapshot.key
                                val firstNameRecipient =
                                    itemSnapshot.child("firstName").getValue(String::class.java)!!
                                val lastNameRecipient =
                                    itemSnapshot.child("lastName").getValue(String::class.java)!!

                                val person = Person(
                                    COUNT_ROWS,
                                    numberRecipient,
                                    firstNameRecipient,
                                    lastNameRecipient,
                                    queryDB)
                                COUNT_ROWS++
                                //operationDao.insertPersons(person)
                                personList.add(person)
                            }
                        }

                        operationDao.insertAllPersons(personList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException()
                    }
                })

        }
    }

    fun addOperation(operation: Operation) {
        val operationsRef = databaseReference.child("Operation")

        updateBalanceUser(operation.send, operation.sum, false)

        operationsRef.child(operation.send + "-"+ operation.time.replace('.', ',')).setValue(operation)

        updateBalanceUser(operation.receive, operation.sum, true)
    }

    private fun updateBalanceUser(number: String, sum: Double, operation: Boolean) {
        databaseReference = FirebaseDatabase.getInstance().reference
        val userRef = databaseReference.child("User")
        val query = userRef.orderByKey().equalTo(number)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {

                    var balance = itemSnapshot.child("balance").getValue(
                        Double::class.java
                    )!!
                    if (operation) {
                        balance += sum
                    } else {
                        balance -= sum
                    }
                    userRef.child(number).child("balance")
                        .setValue((balance * 100.00).roundToInt() / 100.00)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    companion object{
        var userNumber: String = ""

        var COUNT_OPERATION = 1

        var COUNT_ROWS = 1
    }


}