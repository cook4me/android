package ch.epfl.sdp.cook4me.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.BottomBarScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R

enum class Cook4MeScreen(@StringRes val title: Int) {
    Profile(title = R.string.profile),
}

@Composable
fun BottomNavGraph(
    viewModel: WelcomeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
    ){

    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Game.route)
    {
        composable(route = BottomBarScreen.Game.route){
            WelcomeScreen(onStartButtonClicked = {
                viewModel.setName(it)
                navController.navigate(Cook4MeScreen.Profile.name)} )
        }
        composable(route = BottomBarScreen.Home.route){
            ProfileScreen(uiState.name)
        }
        composable(route = BottomBarScreen.Post.route){
            WelcomeScreen(onStartButtonClicked = {
                viewModel.setName(it)
                navController.navigate(Cook4MeScreen.Profile.name)} )
        }
        composable(route = BottomBarScreen.Search.route){
            WelcomeScreen(onStartButtonClicked = {
                viewModel.setName(it)
                navController.navigate(Cook4MeScreen.Profile.name)} )
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(uiState.name)
        }
    }
}