package com.example.laundrybill.myprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laundrybill.NavigationItem
import com.example.laundrybill.addlaundry.clothList
import com.example.laundrybill.convertIsoFormatToDate
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.routeBuilder
import com.example.laundrybill.stringToIntArray

@Composable
fun MyProfileScreen(navController: NavHostController, viewModel: MyProfileViewModel) {

    viewModel.initialize()

    val pendingLaundry: List<Laundry> by viewModel.pendingLaundryList.observeAsState(listOf())
    val pendingBillAmount: Double by viewModel.pendingAmount.observeAsState(0.00)
    val moneySpent: Double by viewModel.totalAmount.observeAsState(0.00)
    val viewMode: Boolean? by viewModel.isDarkMode.observeAsState()

    Column {
        Text(
            "Total Money Spent: \n₹ $moneySpent",
            style = TextStyle(
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            "Pending Bill: ₹ $pendingBillAmount",
            style = TextStyle(
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(bottom = 4.dp),
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .weight(1f)
        ) {
            Text(
                "Pending",
                style = TextStyle(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )

            LazyColumn {
                itemsIndexed(pendingLaundry) { index, laundryItem ->
                    LaundryItemCard(laundryItem, viewModel, navController)
                }
            }
        }
        Row {
            Button(
                onClick = {
                    viewModel.switchMode()
                },
                shape = CircleShape, modifier = Modifier
                    .padding(24.dp)
            ) {
                if (viewMode == true) {
                    Text("Light Mode", style = TextStyle(fontSize = 20.sp))
                } else {
                    Text("Dark Mode", style = TextStyle(fontSize = 20.sp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navController.navigate(NavigationItem.AddLaundry.route)
                },
                shape = CircleShape, modifier = Modifier
                    .padding(24.dp)
                    .size(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Cloth")
            }
        }

    }
}

@Composable
fun LaundryItemCard(
    laundry: Laundry,
    viewModel: MyProfileViewModel,
    navController: NavHostController
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(32.dp))
            .border(border = BorderStroke(1.dp, Color.Blue), shape = RoundedCornerShape(32.dp))
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                convertIsoFormatToDate(laundry.collectionDate),
                style = TextStyle(fontSize = 24.sp), modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.SemiBold
            )

            stringToIntArray(laundry.clothesQuantity).forEachIndexed { index, number->
                if(number != 0){
                    Text(
                        clothList[index].first + " x$number: " + "  ₹ ${number * clothList[index].second}",
                        style = TextStyle(fontSize = 16.sp), modifier = Modifier.padding(6.dp)
                    )
                }
            }

            Row {

                Text(
                    "Number of Clothes: " + laundry.totalClothes,
                    style = TextStyle(fontSize = 16.sp), modifier = Modifier.padding(6.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Total Bill: ₹ " + laundry.totalAmount,
                    style = TextStyle(fontSize = 16.sp), modifier = Modifier.padding(6.dp),fontWeight = FontWeight.SemiBold
                )
            }
            Row(Modifier.padding(vertical = 4.dp, horizontal = 2.dp)) {

                Button(onClick = {
                    viewModel.onCollectedClicked(laundry)
                    viewModel.initialize()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Cloth Collected")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    viewModel.onDeleteLaundryClicked(laundry.itemId)
                    viewModel.initialize()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    navController.navigate(
                        routeBuilder(
                            NavigationItem.AddLaundry.route,
                            laundry.itemId
                        )
                    )
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }

    }
}