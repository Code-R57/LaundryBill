package com.example.laundrybill

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.laundrybill.addlaundry.AddLaundryScreen
import com.example.laundrybill.addlaundry.AddLaundryViewModel
import com.example.laundrybill.addlaundry.AddLaundryViewModelFactory
import com.example.laundrybill.database.LaundryDatabase
import com.example.laundrybill.laundryhistory.LaundryHistoryScreen
import com.example.laundrybill.laundryhistory.LaundryHistoryViewModel
import com.example.laundrybill.laundryhistory.LaundryHistoryViewModelFactory
import com.example.laundrybill.myprofile.MyProfileScreen
import com.example.laundrybill.myprofile.MyProfileViewModel
import com.example.laundrybill.myprofile.MyProfileViewModelFactory
import com.example.laundrybill.ui.theme.LaundryBillTheme

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenSize <= Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }

        val application = requireNotNull(this).application
        val dataSource = LaundryDatabase.getInstance(application).laundryDao

        val myProfileViewModel: MyProfileViewModel by viewModels {
            MyProfileViewModelFactory(
                dataSource,
                application
            )
        }
        val addLaundryViewModel: AddLaundryViewModel by viewModels {
            AddLaundryViewModelFactory(
                dataSource,
                application
            )
        }

        val laundryHistoryViewModel: LaundryHistoryViewModel by viewModels {
            LaundryHistoryViewModelFactory(dataSource, application)
        }

        setContent {

            val isDarkMode: Boolean? by myProfileViewModel.isDarkMode.observeAsState()
            LaundryBillTheme(isDarkMode!!) {
                MainScreen(myProfileViewModel, addLaundryViewModel, laundryHistoryViewModel)
            }
        }
    }

}


@ExperimentalComposeUiApi
@Composable
fun Navigation(
    navController: NavHostController,
    myProfileViewModel: MyProfileViewModel,
    addLaundryViewModel: AddLaundryViewModel,
    historyViewModel: LaundryHistoryViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = NavigationItem.MyProfile.route,
        Modifier.padding(innerPadding)
    ) {
        composable(NavigationItem.MyProfile.route) {
            MyProfileScreen(navController, myProfileViewModel)
        }
        composable(NavigationItem.LaundryHistory.route) {
            LaundryHistoryScreen(historyViewModel)
        }

        composable(
            NavigationItem.AddLaundry.route + "?itemId={itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getLong("itemId")?.let {
                AddLaundryScreen(
                    itemId = it,
                    viewModel = addLaundryViewModel,
                    navController = navController
                )
            }
        }
    }
}


sealed class NavigationItem(var route: String, val label: String) {
    object MyProfile : NavigationItem("myProfile", "My Profile")
    object LaundryHistory : NavigationItem("laundryHistory", "Laundry History")
    object AddLaundry : NavigationItem("addLaundry", "Add/Edit")
}