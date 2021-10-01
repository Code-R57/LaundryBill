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
import com.example.laundrybill.database.Laundry

@Composable
fun AddLaundryScreen(itemId: Long) {
    Column {
        val inputValue: MutableState<String>? = remember { mutableStateOf("01/01/2021") }
        if (itemId == -1L) {
            Text(text = "Add Clothes")
        } else {
            Text("Edit")
        }
        for (cloth in clothList) {
            ClothListInput(cloth, Laundry())
        }
        Row {
            Text("Date of Collection")
            inputValue?.value?.let {
                OutlinedTextField(value = it, onValueChange = { newValue ->
                    inputValue.value = newValue
                })
            }
        }
        Button(onClick = {
            if (itemId == -1L) {
                Log.i("myInfo", "Add")
            } else {
                Log.i("myInfo", "Update")
            }
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