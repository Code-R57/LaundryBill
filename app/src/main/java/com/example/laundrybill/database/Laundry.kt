package com.example.laundrybill.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.laundrybill.addlaundry.clothList
import com.example.laundrybill.intArrayToString

@Entity(tableName = "laundry_data")
data class Laundry(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var itemId: Long = 0L,

    @ColumnInfo(name = "collection_date")
    var collectionDate: String = "2021 01 01",

    @ColumnInfo(name = "total_clothes")
    var totalClothes: Int = 0,

    @ColumnInfo(name = "total_amount")
    var totalAmount: Double = 0.00,

    @ColumnInfo(name = "status")
    var status: String = "Pending",

    @ColumnInfo(name = "clothes_quantity")
    var clothesQuantity: String = intArrayToString(IntArray(clothList.size) { 0 }),

    @ColumnInfo(name = "notification_id")
    var notificationId: Int = 0
)
