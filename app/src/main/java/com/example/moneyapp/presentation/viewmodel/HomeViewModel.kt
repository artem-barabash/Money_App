package com.example.moneyapp.presentation.viewmodel

import androidx.lifecycle.*
import com.example.moneyapp.data.FireBaseManager
import com.example.moneyapp.data.FireBaseManager.Companion.COUNT_OPERATION
import com.example.moneyapp.data.FireBaseManager.Companion.COUNT_ROWS
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.constant.Message
import com.example.moneyapp.domain.entities.Card
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.Person
import com.example.moneyapp.domain.use_cases.SingleTransactionFactory
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.domain.use_cases.UserAccountFactory
import com.example.moneyapp.domain.use_cases.UserAccountFactory.Companion.ACCOUNT
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val _recipient = MutableLiveData<String?>()
    val recipient: LiveData<String?> = _recipient

    private val _cardList = MutableLiveData<List<Card?>>()
    val cardList: LiveData<List<Card?>> = _cardList

    private lateinit var databaseReference:DatabaseReference

    private val listCard = ArrayList<Card?>()

    private val fireBaseManager = FireBaseManager()

    init {
        _user.value = userAccount

        _balance.value = user.value?.user?.balance

        _recipient.value = ""

        initCardList()


        addOperationFromFireBaseToRoom()

    }

    private fun initCardList(){

        listCard.add(Card("master", user.value!!.number, "02/25", user.value!!.user.balance))


        _cardList.value = listCard
    }


    fun getFullUserName():String{
        return (user.value?.user?.firstName ?: "") + " " +(user.value?.user?.lastName ?: "")
    }


    private fun addOperationFromFireBaseToRoom(){
        CoroutineScope(Dispatchers.IO).launch {
            //before delete last operations

            operationDao.deleteAllOperationsRows()
            operationDao.deleteAllPersonsRows()

            retrievedOperationsFromFireBase(SEND)
            retrievedOperationsFromFireBase(RECEIVE)

            operationDao.insertPersons(
                Person(COUNT_ROWS++, ACCOUNT.number, ACCOUNT.number,
                    ACCOUNT.number, "current_user")
            )
        }



    }

    private fun retrievedOperationsFromFireBase(q: String) {
        val number: String = user.value?.number ?: ""

        fireBaseManager.retrieveOperations(q, number, operationDao)
    }

    fun getOperationsAll(): Flow<List<Operation>> = operationDao.getOperationsForUserAll()

    fun getOperationsIncome(number: String): Flow<List<Operation>> = operationDao.getOperationsForUserIncome(number)

    fun getOperationsExpense(number: String): Flow<List<Operation>> = operationDao.getOperationsForUserExpense(number)

    fun getPersons(): Flow<List<Person>> = operationDao.selectPersons()

    fun showCardNumber(number: String?): String? {
        val arrNumber = number?.split("".toRegex())?.toTypedArray()
        val sb = StringBuilder()
        if (arrNumber != null) {
            for (i in arrNumber.indices) {
                sb.append(arrNumber[i])
                if (i % 4 == 0 && i != arrNumber.size - 1) sb.append(" ")
            }
        }
        return sb.toString()
    }

    fun checkNumberInFirebase(number: String){
        databaseReference = FirebaseDatabase.getInstance().reference.child("User")

        val currentNumber = if(number.length != 19) number  else number.replace(" ", "")

        if(currentNumber != ACCOUNT.number.replace(" ", "")){
            val query:Query = databaseReference.orderByKey().equalTo(currentNumber)

            var n = 0
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
                                n,
                                numberRecipient.toString(),
                                firstNameRecipient,
                                lastNameRecipient,
                                ""
                            )
                            n++

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

    fun changePerson(person: Person){
        _recipient.value = showCardNumber(person.number) + " - \n" + person.firstName + " " + person.lastName
    }

    fun sendToRecipient(sumTransaction: Double) {

        _balance.value = _user.value?.user?.balance?.minus(sumTransaction)!!

        ACCOUNT.user.balance -= sumTransaction

        COUNT_OPERATION++

        CoroutineScope(Dispatchers.IO).launch {

            val date = Date()
            val timestamp = Timestamp(date.time)

                //TODO
                //1.номер в базу вноситься не корректно
                //2.привозвращение на Home данные из Firebase заново вносяться, и не видно новой операции

            val otherNumber = _recipient.value.toString().split("-")[0].replace(" ", "")

            val operation = Operation(
                COUNT_OPERATION,
                ACCOUNT.number,
                otherNumber,
                timestamp.toString(),
                sumTransaction
            )

            operationDao.insertOperation(operation)

            fireBaseManager.addOperation(operation)

            SingleTransactionFactory(operation)

        }
    }

    companion object{
        const val RECEIVE : String = "receive"
        const val SEND : String = "send"
    }

}