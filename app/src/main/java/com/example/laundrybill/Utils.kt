package com.example.laundrybill

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun routeBuilder(route: String, itemId: Long): String {
    return ("$route?itemId=$itemId")
}

fun convertDateToIsoFormat(date: String): String {
    val dmy = date.split("/")
    var convertedValue = ""
    for(item in dmy) {
        convertedValue = "$item $convertedValue"
    }
    return convertedValue.trim()
}

fun convertIsoFormatToDate(convertedDate: String): String {
    val ymd = convertedDate.split(" ")
    var date = ""
    ymd.forEachIndexed { index, item ->
        date = if(index == 0) item else "$item/$date"
    }
    return date
}

fun dateFormatter(day: Int, month: Int, year: Int): String {
    var result = ""
    result += "$year"
    result += if(month < 9) " 0${month+1}" else " ${month+1}"
    result += if(day < 10) " 0${day}" else " $day"
    Log.i("myInfo", result)
    return result
}