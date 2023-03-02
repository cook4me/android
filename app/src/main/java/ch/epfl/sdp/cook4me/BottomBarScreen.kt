package ch.epfl.sdp.cook4me

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Game: BottomBarScreen(
        route= "game",
        title= "Game",
        icon = Icons.Default.ShoppingCart
    )
    object Home: BottomBarScreen(
        route= "home",
        title= "Home",
        icon = Icons.Default.Home
    )
    object Post: BottomBarScreen(
        route= "post",
        title= "Post",
        icon = Icons.Default.Add
    )
    object Search: BottomBarScreen(
        route= "search",
        title= "Search",
        icon = Icons.Default.Search
    )
    object Profile: BottomBarScreen(
        route= "profile",
        title= "Profile",
        icon = Icons.Default.Person
    )
}
