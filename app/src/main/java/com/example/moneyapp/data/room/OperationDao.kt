package com.example.moneyapp.data.room

import androidx.room.*
import com.example.moneyapp.domain.entities.Operation
import kotlinx.coroutines.flow.Flow


@Dao
interface OperationDao {

    //@Insert
    //fun insertOperation(operation: Operation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(listOperation: List<Operation>)

    @Query("SELECT * FROM operations WHERE send_number = :number OR receive_number = :number")
    fun getOperationsForUser(number: String) : Flow<List<Operation>>

    @Query("DELETE FROM operations")
    fun deleteAllRows()
}