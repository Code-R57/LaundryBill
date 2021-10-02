package com.example.laundrybill

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
import com.example.laundrybill.ui.theme.Purple500

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
fun MainScreen(
    myProfileViewModel: MyProfileViewModel,
    addLaundryViewModel: AddLaundryViewModel,
    laundryHistoryViewModel: LaundryHistoryViewModel
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val items = listOf(
        NavigationItem.MyProfile,
        NavigationItem.LaundryHistory
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar() },
        bottomBar = {
            BottomNavigation(contentColor = Color.White, backgroundColor = Purple500) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { navigationItem ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navigationItem.route } == true,
                        icon = {
                            if (navigationItem == NavigationItem.MyProfile) Icon(
                                Icons.Filled.AccountCircle,
                                contentDescription = "My Profile",
                            ) else Icon(Icons.Filled.List, contentDescription = "Laundry History")
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }, label = { Text(navigationItem.label) })
                }
            }
        }
    ) {
        Navigation(
            navController,
            myProfileViewModel,
            addLaundryViewModel,
            laundryHistoryViewModel,
            it
        )
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = Purple500,
        contentColor = Color.White,
    )
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
        ) {
            it.arguments?.getLong("itemId")?.let {
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