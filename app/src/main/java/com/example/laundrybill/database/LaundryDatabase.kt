package com.example.laundrybill.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Laundry::class], version = 1, exportSchema = false)
abstract class LaundryDatabase : RoomDatabase() {

    abstract val laundryDao: LaundryDao

    companion object {

        @Volatile
        private var INSTANCE: LaundryDatabase? = null

        fun getInstance(context: Context): LaundryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LaundryDatabase::class.java,
                        "laundry_history_data"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}