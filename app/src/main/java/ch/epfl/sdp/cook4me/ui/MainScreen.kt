package ch.epfl.sdp.cook4me.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.BottomBarScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold (
        bottomBar = { BottomBar(navController = navController)}){
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Post,
        BottomBarScreen.Game,
        BottomBarScreen.Profile,
        BottomBarScreen.Search
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState() //observes navBackStack entry
    val currentDestination = navBackStackEntry?.destination //get the destination

    BottomNavigation { //add items to our bottom navigation, we can specify elevation and colors
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    BottomNavigationItem(
        label = {
        Text(text = screen.title)
        },
        icon = {
         Icon(imageVector = screen.icon,
             contentDescription = "Navigation Icon")
        },
        selected = currentDestination?.hierarchy?.any{
         it.route == screen.route
        } == true, //tells if current item is selected or not
        onClick = {
             navController.navigate(screen.route) //@DEBUG route==post if click on post
        }
    )
}