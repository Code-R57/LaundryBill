package com.example.laundrybill.laundryhistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.laundrybill.database.Laundry

@Composable
fun LaundryHistoryScreen() {

    val laundryHistory: List<Laundry> =
        listOf(Laundry(status = "Completed"), Laundry(status = "Completed"))

    Column {
        Text("Clothes Collected")
        LazyColumn {
            itemsIndexed(laundryHistory) { index, laundryItem ->
                LaundryHistoryItemCard(laundry = laundryItem)
            }
        }
    }
}

@Composable
fun LaundryHistoryItemCard(laundry: Laundry) {
    Card {
        Column {
            Text(laundry.collectionDate)
            Text("Number of Clothes: " + laundry.totalClothes)
            Text("Total Bill: " + laundry.totalAmount)
        }
    }
}