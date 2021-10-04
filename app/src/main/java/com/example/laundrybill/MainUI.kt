package com.example.laundrybill

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.laundrybill.addlaundry.AddLaundryViewModel
import com.example.laundrybill.laundryhistory.LaundryHistoryViewModel
import com.example.laundrybill.myprofile.MyProfileViewModel
import com.example.laundrybill.ui.theme.Purple500

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