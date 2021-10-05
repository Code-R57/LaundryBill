package com.example.laundrybill.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

fun setNotification(
    alarmManager: AlarmManager,
    context: Context,
    calendar: Calendar,
    notificationId: Int
) {

    val intent = Intent(context, NotificationBroadcast::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId, intent, 0
    )

    val currentDate = Calendar.getInstance()
    val difference = Calendar.getInstance()
    difference.timeInMillis = calendar.timeInMillis - currentDate.timeInMillis

    alarmManager.setExact(
        AlarmManager.RTC, System.currentTimeMillis() + difference.timeInMillis, pendingIntent
    )
}

fun cancelNotification(
    alarmManager: AlarmManager,
    context: Context,
    notificationId: Int
) {

    val intent = Intent(context, NotificationBroadcast::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId, intent, 0
    )

    alarmManager.cancel(pendingIntent)
}