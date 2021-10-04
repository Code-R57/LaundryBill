package com.example.laundrybill.addlaundry

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.laundrybill.*
import com.example.laundrybill.database.Laundry

@ExperimentalComposeUiApi
@Composable
fun AddLaundryScreen(
    itemId: Long,
    viewModel: AddLaundryViewModel,
    navController: NavHostController
) {

    viewModel.initialize(itemId)
    val laundryItem: Laundry by viewModel.laundryItem.observeAsState(Laundry())
    val currentDate: String? by viewModel.currentDate.observeAsState()
    var clothNumber: Array<Int> = arrayOf(0, 0, 0, 0)

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
        clothList.forEachIndexed { index, cloth ->
            ClothListInput(cloth, clothNumber, index)
        }
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
            Button(onClick = {
                MaterialDialog(context).show {
                    datePicker { _, date ->
                        laundryItem.collectionDate = dateFormatter(date.dayOfMonth, date.month, date.year)
                    }
                }
            }, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.DateRange, "Calendar")
            }
        }
        Button(
            onClick = {
                laundryItem.totalClothes = 0
                laundryItem.totalAmount = 0.00
                laundryItem.clothesQuantity = ""
                clothNumber.forEachIndexed { index, number ->
                    laundryItem.totalClothes += number
                    laundryItem.totalAmount += (clothList[index].second * number)
                    laundryItem.clothesQuantity += "$number "
                    Log.i("myInfo", laundryItem.clothesQuantity)
                }
                if (itemId == -1L) {
                    viewModel.onAddClicked(laundryItem)

                } else {
                    viewModel.onEditClicked(laundryItem)
                }
                navController.navigate(NavigationItem.MyProfile.route)
            }, modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Text(if (itemId == -1L) "Add" else "Edit", style = TextStyle(fontSize = 20.sp))
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ClothListInput(cloth: Pair<String, Double>, clothNumber: Array<Int>, index: Int) {
    val inputValue = remember { mutableStateOf( "") }
    Box {
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    cloth.first, style = TextStyle(
                        fontSize = 22.sp
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
    Pair("T-Shirt", 25.00),
    Pair("Pants", 20.00),
    Pair("Bed Sheets", 50.00),
    Pair("Towel", 30.00)
)