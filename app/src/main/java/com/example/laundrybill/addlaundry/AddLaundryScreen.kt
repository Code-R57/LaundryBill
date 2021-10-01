package com.example.laundrybill.addlaundry

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.database.Laundry

@Composable
fun AddLaundryScreen(itemId: Long, viewModel: AddLaundryViewModel, navController: NavHostController) {

    var laundryItem = Laundry()
    if(itemId != -1L) laundryItem.itemId = itemId
    Column {
        var inputValue: MutableState<String>? = remember { mutableStateOf("01/01/2021") }
        if (itemId == -1L) {
            Text(text = "Add Clothes")
        } else {
            Text("Edit")
            inputValue = remember { mutableStateOf(laundryItem.collectionDate) }
        }
        for (cloth in clothList) {
            ClothListInput(cloth, Laundry())
        }
        Row {
            Text("Date of Collection")
            inputValue?.value?.let {
                OutlinedTextField(value = it, onValueChange = { newValue ->
                    inputValue.value = newValue
                    laundryItem.collectionDate = inputValue.value
                })
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
        }) {
            Text(if (itemId == -1L) "Add" else "Edit")
        }
        Text("$itemId")
    }
}

@Composable
fun ClothListInput(cloth: Pair<String, Double>, laundryCloth: Laundry) {
    val inputValue = remember { mutableStateOf("0") }
    Row {
        Column {
            Text(cloth.first)
            Text("Rate: ${cloth.second}")
        }
        OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
            inputValue.value = newValue
        })
    }
}

val clothList = listOf(
    Pair("T-Shirt", 25.00),
    Pair("Pants", 20.00),
    Pair("Bed Sheets", 50.00),
    Pair("Towel", 30.00)
)