package com.example.laundrybill.myprofile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.laundrybill.database.Laundry

@Composable
fun MyProfileScreen(navController: NavHostController) {

    var pendingLaundry: List<Laundry> = listOf(Laundry(), Laundry())
    var pendingBillAmount: Double = 10.00
    var moneySpent: Double = 15.00

    Column {
        Text("Total Money Spent: $moneySpent")
        Text("Pending Bill: $pendingBillAmount")
        Text("To Be Collected")

        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            itemsIndexed(pendingLaundry) { index, laundryItem ->
                LaundryItemCard(laundryItem)
            }
        }
        Button(
            onClick = { },
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Cloth")

        }

    }
}

@Composable
fun LaundryItemCard(laundry: Laundry) {
    Card {
        Row {
            Column {
                Text("Number of Clothes: " + laundry.totalClothes)
                Text("Collection Date: " + laundry.collectionDate)
                Text("Total Bill: " + laundry.totalAmount)
                Row {

                    Button(onClick = { Log.i("myInfo", "Button Collect") }) {
                        Icon(Icons.Default.Check, contentDescription = "Cloth Collected")
                    }

                    Button(onClick = { Log.i("myInfo", "Button Delete") }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    Button(onClick = { Log.i("myInfo", "Button Edit") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }

        }
    }
}