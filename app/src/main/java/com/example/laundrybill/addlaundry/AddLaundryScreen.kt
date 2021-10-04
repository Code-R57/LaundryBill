package com.example.laundrybill.addlaundry

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.convertIsoFormatToDate
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.dateFormatter
import com.example.laundrybill.notification.NotificationBroadcast
import com.example.laundrybill.stringToIntArray
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun AddLaundryScreen(
    itemId: Long,
    viewModel: AddLaundryViewModel,
    navController: NavHostController,
    alarmManager: AlarmManager, context: Context
) {

    viewModel.initialize(itemId)
    val laundryItem: Laundry by viewModel.laundryItem.observeAsState(Laundry())
    val currentDate: String? by viewModel.currentDate.observeAsState()
    val clothNumber = IntArray(clothList.size) { 0 }

    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        val date = convertIsoFormatToDate(if (currentDate != null) currentDate!! else "01 01 2021")
        remember { mutableStateOf(date) }

        if (itemId == -1L) {
            Text(
                text = "Add Clothes", style = TextStyle(
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp, bottom = 16.dp, top = 20.dp)
            )
        } else {
            Text(
                "Edit", style = TextStyle(
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                CalendarInput(laundryItem)
            }
            val values = stringToIntArray(laundryItem.clothesQuantity)
            itemsIndexed(clothList) { index, cloth ->
                ClothListInput(cloth, clothNumber, index, values[index].toString())
            }

            item {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            laundryItem.totalClothes = 0
                            laundryItem.totalAmount = 0.00
                            laundryItem.clothesQuantity = ""
                            clothNumber.forEachIndexed { index, number ->
                                laundryItem.totalClothes += number
                                laundryItem.totalAmount += (clothList[index].second * number)
                                laundryItem.clothesQuantity += "$number "
                            }
                            if (itemId == -1L) {
                                viewModel.onAddClicked(laundryItem)

                            } else {
                                viewModel.onEditClicked(laundryItem)
                            }
                            navController.navigate(NavigationItem.MyProfile.route)
                        }, modifier = Modifier
                            .padding(16.dp), shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            if (itemId == -1L) "Add" else "Edit",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        val calendarDate = Calendar.getInstance()
                        val ymd = laundryItem.collectionDate.split(" ")
                        calendarDate.set(ymd[0].toInt(), ymd[1].toInt() - 1, ymd[2].toInt())
                        if (laundryItem.collectionDate != "2021 01 01")
                            setNotification(alarmManager, context, calendarDate)
                    }
                }
            }
        }

    }
}

@Composable
private fun CalendarInput(laundryItem: Laundry) {
    Row(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Date of Collection", style = TextStyle(
                fontSize = 22.sp
            ), modifier = Modifier
                .padding(4.dp)
                .padding(horizontal = 6.dp)
        )
        val context = LocalContext.current
        val date = laundryItem.collectionDate.split(" ")
        val currentDate: Calendar = Calendar.getInstance()
        currentDate.set(date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
        Button(onClick = {
            MaterialDialog(context).show {
                datePicker(minDate = Calendar.getInstance()) { _, date ->
                    laundryItem.collectionDate =
                        dateFormatter(date.dayOfMonth, date.month, date.year)
                }
            }
        }, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.Default.DateRange, "Calendar")
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ClothListInput(cloth: Pair<String, Double>, clothNumber: IntArray, index: Int, value: String) {
    val inputValue = remember { mutableStateOf(value) }
    Box(contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    cloth.first, style = TextStyle(
                        fontSize = 18.sp
                    ), modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                )
                Text(
                    "Rate: ₹ ${cloth.second}", style = TextStyle(
                        fontSize = 14.sp
                    ), modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                )
            }
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
                inputValue.value = newValue
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ), keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ))
            if (inputValue.value != "") {
                try {
                    if (inputValue.value.toInt() > 0) clothNumber[index] = inputValue.value.toInt()

                } catch (e: Exception) {
                    Toast.makeText(
                        LocalContext.current,
                        "Please Enter a Number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}

val clothList = listOf(
    Pair("T-Shirt", 10.00),
    Pair("Shirt", 12.00),
    Pair("Pant", 12.00),
    Pair("Jeans", 15.00),
    Pair("Bed Sheet", 15.00),
    Pair("Pillow Cover", 8.00),
    Pair("Towel", 10.00),
    Pair("Jacket", 18.00),
    Pair("Blanket", 40.00)
)

fun setNotification(alarmManager: AlarmManager, context: Context, calendar: Calendar) {

    val intent = Intent(context, NotificationBroadcast::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        System.currentTimeMillis().toInt(), intent, 0
    )

    val currentDate = Calendar.getInstance()
    val difference = Calendar.getInstance()
    difference.timeInMillis = calendar.timeInMillis - currentDate.timeInMillis

    alarmManager.setExact(
        AlarmManager.RTC, System.currentTimeMillis() + difference.timeInMillis, pendingIntent
    )
}