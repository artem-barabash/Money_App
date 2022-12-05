package com.example.moneyapp.data.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moneyapp.domain.entities.Operation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var db: AppDatabase
    private lateinit var dao: OperationDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()

        /*db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()*/

        dao = db.operationDao()
    }

    @After
    fun closeDB(){
        //dao.deleteAllOperationsRows()
        db.close()
    }

    @Test
    fun add_operation() = runBlocking{
        val date = Date()
        val timestamp = Timestamp(date.time)

        val operation = Operation(1, "1235567950916625", "1235567950916621",timestamp.toString(), 90.0)

        dao.insertOperation(operation)
    }


    @Test
    fun insert_all_operations() = runBlocking{
        val arrayList = ArrayList<Operation>()
        val date = Date()
        val timestamp = Timestamp(date.time)


        arrayList.add(
            Operation(1,
            "1235567950916625",
            "1235567950916621",
            timestamp.toString(),
            90.0)
        )


        arrayList.add(Operation(2,
                "1235567950916625",
                "1235567950916622",
                timestamp.toString(),
                9.0)
        )

        dao.insertAll(arrayList)

    }


    @Test
    fun select_operations() = runBlocking{
        //insert_all_operations()

            val date = Date()
            val timestamp = Timestamp(date.time)
            val operation = Operation(2,
                "1235567950916625",
                "1235567950916622",
                timestamp.toString(),
                9.0)

        dao.insert(operation)

        //delay(5000)
        /*val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getOperationsForUserAll().collect{
                println("elemem =$it")

                //assertEquals(9.0, it[0].sum)
                latch.countDown()
            }
        }


        latch.await()
        job.cancelAndJoin()*/
        //assertEquals(2, list!!.size)
    }

}