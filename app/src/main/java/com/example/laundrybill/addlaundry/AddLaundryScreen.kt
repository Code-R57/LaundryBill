package com.example.laundrybill.addlaundry

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.database.Laundry

@Composable
fun AddLaundryScreen(
    itemId: Long,
    viewModel: AddLaundryViewModel,
    navController: NavHostController
) {

    viewModel.initialize(itemId)
    val laundryItem: Laundry by viewModel.laundryItem.observeAsState(Laundry())
    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()) {
        val inputValue: MutableState<String>?
        if (itemId == -1L) {
            Text(
                text = "Add Clothes", style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp, bottom = 16.dp, top = 20.dp)
            )
            inputValue = remember { androidx.compose.runtime.mutableStateOf("01/01/2021") }
        } else {
            Text(
                "Edit", style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
            inputValue = remember { mutableStateOf(laundryItem.collectionDate) }
        }
        for (cloth in clothList) {
            ClothListInput(cloth, Laundry())
        }
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()) {
            Text("Date of Collection",style = TextStyle(
                color = Color.LightGray,
                fontSize = 18.sp
            ), modifier = Modifier
                .padding(4.dp)
                .width(115.dp)
                .padding(horizontal = 6.dp))
            inputValue?.value?.let {
                OutlinedTextField(value = it, onValueChange = { newValue ->
                    inputValue.value = newValue
                })
            }
            if (inputValue != null) {
                laundryItem.collectionDate = inputValue.value
            }
        }
        Button(onClick = {
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
            .padding(16.dp)) {
            Text(if (itemId == -1L) "Add" else "Edit", style = TextStyle(fontSize = 20.sp))
        }
        Log.i("myInfo","$itemId")
    }
}

@Composable
fun ClothListInput(cloth: Pair<String, Double>, laundryCloth: Laundry) {
    val inputValue = remember { mutableStateOf("0") }
    Box{
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()) {
            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    cloth.first, style = TextStyle(
                        color = Color.LightGray,
                        fontSize = 22.sp
                    ), modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                )
                Text(
                    "Rate: ₹ ${cloth.second}", style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    ), modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                )
            }
            OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
                inputValue.value = newValue
            })
        }
    }
}

val clothList = listOf(
    Pair("T-Shirt", 25.00),
    Pair("Pants", 20.00),
    Pair("Bed Sheets", 50.00),
    Pair("Towel", 30.00)
)