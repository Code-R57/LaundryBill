package com.example.laundrybill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            LaundryBillTheme {
                MainScreen(myProfileViewModel, addLaundryViewModel, laundryHistoryViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(myProfileViewModel: MyProfileViewModel, addLaundryViewModel: AddLaundryViewModel, laundryHistoryViewModel: LaundryHistoryViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerBackgroundColor = Color.Blue,
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },
    ) {
        Navigation(navController, myProfileViewModel, addLaundryViewModel, laundryHistoryViewModel)
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "Navigation Menu")
            }
        },
        backgroundColor = Color.Blue,
        contentColor = Color.White
    )
}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavigationItem.MyProfile,
        NavigationItem.LaundryHistory
    )
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(colorResource(id = R.color.purple_500)),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Menu",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            })
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DrawerItem(item: NavigationItem, selected: Boolean, onItemClick: (NavigationItem) -> Unit) {
    val background = if (selected) R.color.black else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    myProfileViewModel: MyProfileViewModel,
    addLaundryViewModel: AddLaundryViewModel,
    historyViewModel: LaundryHistoryViewModel
) {
    NavHost(navController, startDestination = NavigationItem.MyProfile.route) {
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


sealed class NavigationItem(var route: String, var title: String) {
    object MyProfile : NavigationItem("myProfile", "My Profile")
    object LaundryHistory : NavigationItem("laundryHistory", "Laundry History")
    object AddLaundry : NavigationItem("addLaundry", "Add/Edit Laundry")
}