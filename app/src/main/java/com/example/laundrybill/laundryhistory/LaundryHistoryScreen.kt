package com.example.laundrybill.laundryhistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.laundrybill.database.Laundry

@Composable
fun LaundryHistoryScreen(viewModel: LaundryHistoryViewModel) {

    val laundryHistory: List<Laundry> by viewModel.collectedLaundryList.observeAsState(listOf())

    viewModel.initialize()

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