package com.example.laundrybill.addlaundry

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.convertDateToIsoFormat
import com.example.laundrybill.convertIsoFormatToDate
import com.example.laundrybill.database.Laundry

@Composable
fun AddLaundryScreen(
    itemId: Long,
    viewModel: AddLaundryViewModel,
    navController: NavHostController
) {

    viewModel.initialize(itemId)
    val laundryItem: Laundry by viewModel.laundryItem.observeAsState(Laundry())
    var clothNumber: Array<Int> = arrayOf(0, 0, 0, 0)

    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Log.i("myInfo", laundryItem.collectionDate +"   " + convertIsoFormatToDate(laundryItem.collectionDate))
        val date = convertIsoFormatToDate(laundryItem.collectionDate)
        val inputValue: MutableState<String> =
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
                    fontSize = 18.sp
                ), modifier = Modifier
                    .padding(4.dp)
                    .width(115.dp)
                    .padding(horizontal = 6.dp)
            )
            inputValue.value.let {
                OutlinedTextField(value = it, onValueChange = { newValue ->
                    inputValue.value = newValue
                })
            }
            laundryItem.collectionDate = convertDateToIsoFormat(inputValue.value)
        }
        Button(
            onClick = {
                laundryItem.totalClothes = 0
                laundryItem.totalAmount = 0.00
                clothNumber.forEachIndexed { index, number ->
                    laundryItem.totalClothes += number
                    laundryItem.totalAmount += (clothList[index].second * number)
                }
                if (itemId == -1L) {
                    Log.i("myInfo", "Add")
                    viewModel.onAddClicked(laundryItem)

                } else {
                    Log.i("myInfo", "Update")
                    viewModel.onEditClicked(laundryItem)
                }
                navController.navigate(NavigationItem.MyProfile.route)
            }, modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Text(if (itemId == -1L) "Add" else "Edit", style = TextStyle(fontSize = 20.sp))
        }
        Log.i("myInfo", "$itemId")
    }
}

@Composable
fun ClothListInput(cloth: Pair<String, Double>, clothNumber: Array<Int>, index: Int) {
    val inputValue = remember { mutableStateOf("0") }
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
            OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
                inputValue.value = newValue
            })
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