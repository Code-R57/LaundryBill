package com.example.laundrybill

import android.util.Log

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