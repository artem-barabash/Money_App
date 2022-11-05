package com.example.moneyapp.data.room

import androidx.room.*
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.Person
import kotlinx.coroutines.flow.Flow


@Dao
interface OperationDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(listOperation: List<Operation>)

    @Query("SELECT * FROM operations")
    fun getOperationsForUserAll(number: String) : Flow<List<Operation>>

    @Query("SELECT operations.id, persons.first_name AS send_number, operations.receive_number,  operations.time_operation, operations.sum_operation " +
            " FROM operations" +
            " JOIN persons " +
            "ON operations.send_number = persons.person_number" +
            " WHERE receive_number = :number AND persons.status = 'receive'")
    //@Query("SELECT * FROM operations WHERE receive_number = :number")
    fun getOperationsForUserIncome(number: String) : Flow<List<Operation>>


    @Query("SELECT operations.id, operations.send_number, persons.first_name AS receive_number, operations.time_operation, operations.sum_operation " +
            " FROM operations" +
            " JOIN persons " +
            "ON operations.receive_number = persons.person_number" +
            " WHERE send_number = :number AND persons.status = 'send'")
    //@Query("SELECT * FROM operations WHERE send_number = :number")
    fun getOperationsForUserExpense(number: String) : Flow<List<Operation>>

    @Query("DELETE FROM operations")
    fun deleteAllOperationsRows()

    @Insert
    fun insertOperation(operation: Operation)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAllPersons(listOperation: List<Person>)

    @Query("DELETE FROM persons")
    fun deleteAllPersonsRows()

    //@Query("DELETE FROM persons WHERE id NOT IN (SELECT MIN(id) FROM persons GROUP BY person_number)")
    //fun deleteSimilarElementsFromPersons()

}