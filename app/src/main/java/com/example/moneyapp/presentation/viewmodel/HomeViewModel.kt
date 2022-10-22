package com.example.moneyapp.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.example.moneyapp.data.room.OperationDao
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.use_cases.UserAccount
import com.example.moneyapp.presentation.adapter.OperationAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(userAccount: UserAccount, private val operationDao: OperationDao): ViewModel() {

    private val _user = MutableLiveData<UserAccount?>()
    val user: LiveData<UserAccount?> = _user

    private val _balance = MutableLiveData(0.0)
    val balance: LiveData<String> = Transformations.map(_balance){
        NumberFormat.getCurrencyInstance(Locale("en", "US")).format(it)
    }

    private var countOperation = 0


    init {
        _user.value = userAccount

        _balance.value = user.value?.user?.balance
    }

    fun getFullUserName():String{
        return (user.value?.user?.firstName ?: "") + " " +(user.value?.user?.lastName ?: "")
    }

    fun getListSeparatedByDayOperation(list: ArrayList<Operation>) {
        var dateOperation = getLocalDate(list[0])!!

        val listOperation = ArrayList<Operation>()


        for(i in 0 until list.size){
            if(getLocalDate(list[i]) == dateOperation){
                listOperation.add(list[i])
            }else{
                println("$dateOperation $listOperation")
                //println("${dateOperation.toString()}")
                dateOperation = getLocalDate(list[i])!!
                listOperation.clear()
                listOperation.add(list[i])
            }
        }

    }

    fun addOperationFromFireBaseToRoom(){
        //before delete last operations


        CoroutineScope(Dispatchers.IO).launch {
            operationDao.deleteAllRows()

            retrievedOperationsFromFireBase("receive")
            retrievedOperationsFromFireBase("send")
        }

    }

    private fun retrievedOperationsFromFireBase(q: String) {

        val number:String = user.value?.number ?: ""

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val operationRef = databaseReference.child("Operation")



        operationRef.orderByChild(q).equalTo(number)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot!!.exists()){
                        val list = ArrayList<Operation>()

                        for(h in snapshot.children){

                            val sum:Any? = h.child("sum").value

                            val nSum : Double = if(sum != null && sum.toString() != "0"){
                                sum.toString().toDouble()
                            }else 0.0




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
                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException()
                }
            })
    }


    fun getOperations(number: String): Flow<List<Operation>> = operationDao.getOperationsForUser(number)

    @SuppressLint("NewApi")
    private fun getLocalDate(operation: Operation): LocalDate? {
        val date = operation.time.split(" ")[0]
        return LocalDate.parse(date)
    }

}