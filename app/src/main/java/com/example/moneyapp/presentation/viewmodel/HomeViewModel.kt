package com.example.moneyapp.presentation.viewmodel

import android.provider.Settings.System.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.constant.Message
import com.example.moneyapp.domain.entities.Card
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.Person
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.*

class HomeViewModel(userAccount: UserAccount, private val operationDao: OperationDao): ViewModel() {

    private val _user = MutableLiveData<UserAccount?>()
    val user: LiveData<UserAccount?> = _user

    private val _balance = MutableLiveData(0.0)
    val balance: LiveData<String> = Transformations.map(_balance){
        NumberFormat.getCurrencyInstance(Locale("en", "US")).format(it)
    }

    private var countOperation = 0

    private val _recipient = MutableLiveData<String?>()
    val recipient: LiveData<String?> = _recipient



    private val _cardList = MutableLiveData<List<Card?>>()
    val cardList: LiveData<List<Card?>> = _cardList

    private lateinit var databaseReference:DatabaseReference

    private val listCard = ArrayList<Card?>()

    private var getData: Boolean = false

    init {
        _user.value = userAccount

        _balance.value = user.value?.user?.balance

        _recipient.value = ""

        initCardList()
    }

    private fun initCardList(){

        listCard.add(Card("master", user.value!!.number, "02/25", user.value!!.user.balance))


        _cardList.value = listCard
    }


    fun getFullUserName():String{
        return (user.value?.user?.firstName ?: "") + " " +(user.value?.user?.lastName ?: "")
    }


    fun addOperationFromFireBaseToRoom(){
        if(!getData){
            CoroutineScope(Dispatchers.IO).launch {
                //before delete last operations

                operationDao.deleteAllRows()

                retrievedOperationsFromFireBase("receive")
                retrievedOperationsFromFireBase("send")
                getData = true
            }
        }


    }

    private fun retrievedOperationsFromFireBase(q: String) {


            val number: String = user.value?.number ?: ""

            databaseReference = FirebaseDatabase.getInstance().reference
            val operationRef = databaseReference.child("Operation")



            operationRef.orderByChild(q).equalTo(number)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot!!.exists()) {
                            val list = ArrayList<Operation>()

                            for (h in snapshot.children) {

                                val sum: Any? = h.child("sum").value

                                val nSum: Double = if (sum != null && sum.toString() != "0") {
                                    sum.toString().toDouble()
                                } else 0.0


                                val operation = Operation(
                                    countOperation,
                                    h.child("send").getValue(String::class.java)!!,
                                    h.child("receive").getValue(String::class.java)!!,
                                    h.child("time").getValue(String::class.java)!!,
                                    nSum
                                )

                                list.add(operation)
                                countOperation++
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                operationDao.insertAll(list)
                                getData = true
                            }


                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException()
                    }
                })

    }


    fun getOperationsAll(number: String): Flow<List<Operation>> = operationDao.getOperationsForUserAll(number)

    fun getOperationsIncome(number: String): Flow<List<Operation>> = operationDao.getOperationsForUserIncome(number)

    fun getOperationsExpense(number: String): Flow<List<Operation>> = operationDao.getOperationsForUserExpense(number)

    fun showCardNumber(number: String): String? {
        val arrNumber = number.split("".toRegex()).toTypedArray()
        val sb = StringBuilder()
        for (i in arrNumber.indices) {
            sb.append(arrNumber[i])
            if (i % 4 == 0 && i != arrNumber.size - 1) sb.append(" ")
        }
        return sb.toString()
    }

    fun checkNumberInFirebase(number: String){
        databaseReference = FirebaseDatabase.getInstance().reference.child("User")

        val currentNumber = if(number.length != 19) number  else number.trim()

        if(currentNumber != ACCOUNT.number.trim()){
            val query:Query = databaseReference.orderByKey().equalTo(currentNumber)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()) {

                        for (itemSnapshot in snapshot.children){

                            val numberRecipient = itemSnapshot.key
                            val firstNameRecipient =
                                itemSnapshot.child("firstName").getValue(String::class.java)!!
                            val lastNameRecipient =
                                itemSnapshot.child("lastName").getValue(String::class.java)!!

                            val recipientPerson = Person(
                                numberRecipient.toString(),
                                firstNameRecipient,
                                lastNameRecipient
                            )

                            _recipient.value =
                                showCardNumber(recipientPerson.number) + " - \n" + recipientPerson.firstName + " " + recipientPerson.lastName
                        }
                    }else {
                        _recipient.value = Message.noCorrectRecipientNumber
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                    error.toException()
                }

            })
        }

    }

    fun closeTransaction(){
        _recipient.value = ""
    }

    fun sendToRecipient(sumTransaction: Double) {
        if(recipient.value != Message.noCorrectRecipientNumber || recipient.value != ""){
            _balance.value = _user.value?.user?.balance?.minus(sumTransaction)!!

            ACCOUNT.user.balance -= sumTransaction

            countOperation++

            CoroutineScope(Dispatchers.IO).launch {

                val date = Date()
                val timestamp = Timestamp(date.time)

                //TODO
                //1.номер в базу вноситься не корректно
                //2.привозвращение на Home данные из Firebase заново вносяться, и не видно новой операции

                val otherNumber = _recipient.value.toString().trim().split("-")[0]

                val operation = Operation(
                    countOperation,
                    ACCOUNT.number,
                    otherNumber,
                    timestamp.toString(),
                    sumTransaction
                )

                operationDao.insertOperation(operation)
            }

            println("ACCOUNT = $ACCOUNT")
        }
    }

}