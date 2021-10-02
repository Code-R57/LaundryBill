package com.example.laundrybill.laundryhistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laundrybill.convertIsoFormatToDate
import com.example.laundrybill.database.Laundry

@Composable
fun LaundryHistoryScreen(viewModel: LaundryHistoryViewModel) {

    val laundryHistory: List<Laundry> by viewModel.collectedLaundryList.observeAsState(listOf())

    viewModel.initialize()

    Column {
        Text(
            "Clothes Collected", style = TextStyle(
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
        ) {
            itemsIndexed(laundryHistory) { index, laundryItem ->
                LaundryHistoryItemCard(laundry = laundryItem)
            }
        }
    }
}

@Composable
fun LaundryHistoryItemCard(laundry: Laundry) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                convertIsoFormatToDate(laundry.collectionDate),
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(6.dp)
            )
            Row {
                Text(
                    "Number of Clothes: " + laundry.totalClothes,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(6.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Total Bill: ₹ " + laundry.totalAmount,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}