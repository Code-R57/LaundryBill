package com.example.laundrybill.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LaundryDao {

    @Insert
    fun insertLaundryItem(laundry: Laundry)

    @Update
    fun updateLaundryItem(laundry: Laundry)

    @Query("DELETE FROM laundry_data WHERE item_id = :itemId")
    fun deleteLaundryItem(itemId: Long)

    @Query("SELECT * FROM laundry_data ORDER BY item_id DESC")
    fun getAllLaundryItem(): List<Laundry>?

    @Query("SELECT * FROM laundry_data WHERE status = 'Pending' ORDER BY collection_date ASC")
    fun getPendingLaundryItem(): List<Laundry>?

    @Query("SELECT * FROM laundry_data WHERE status = 'Collected' ORDER BY collection_date ASC")
    fun getCollectedLaundryItem(): List<Laundry>?

    @Query("SELECT * FROM laundry_data WHERE item_id = :itemId")
    fun getLaundryItem(itemId: Long): Laundry

}