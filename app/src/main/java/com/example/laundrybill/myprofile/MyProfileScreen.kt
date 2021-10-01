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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.database.Laundry

@Composable
fun MyProfileScreen(navController: NavHostController, viewModel: MyProfileViewModel) {

    val pendingLaundry: List<Laundry> by viewModel.pendingLaundryList.observeAsState(listOf())
    val pendingBillAmount: Double by viewModel.pendingAmount.observeAsState(0.00)
    val moneySpent: Double by viewModel.totalAmount.observeAsState(0.00)

    Column {
        Text("Total Money Spent: $moneySpent")
        Text("Pending Bill: $pendingBillAmount")
        Text("To Be Collected")

        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            itemsIndexed(pendingLaundry) { index, laundryItem ->
                LaundryItemCard(laundryItem, viewModel, navController)
            }
        }
        Button(
            onClick = { navController.navigate(NavigationItem.AddLaundry.route) },
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Cloth")

        }

    }
}

@Composable
fun LaundryItemCard(laundry: Laundry, viewModel: MyProfileViewModel, navController: NavHostController) {
    Card {
        Row {
            Column {
                Text("Number of Clothes: " + laundry.totalClothes)
                Text("Collection Date: " + laundry.collectionDate)
                Text("Total Bill: " + laundry.totalAmount)
                Row {

                    Button(onClick = { Log.i("myInfo", "Button Collect")
                    viewModel.onCollectedClicked(laundry)}) {
                        Icon(Icons.Default.Check, contentDescription = "Cloth Collected")
                    }

                    Button(onClick = { Log.i("myInfo", "Button Delete")
                    viewModel.onDeleteLaundryClicked(laundry.itemId)}) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    Button(onClick = { Log.i("myInfo", "Button Edit")
                    navController.navigate(NavigationItem.AddLaundry.route + "?itemId={${laundry.itemId}}")}) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }

        }
    }
}