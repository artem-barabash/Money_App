package com.example.moneyapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneyapp.domain.entities.Operation
import com.example.moneyapp.domain.entities.Person

//@Database(entities = [Operation::class, Person::class], version = 1)
@Database(entities = [Operation::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun operationDao(): OperationDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context:Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/user_data.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}