package ch.epfl.sdp.cook4me

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.ProfileScreen
import ch.epfl.sdp.cook4me.ui.WelcomeScreen
import ch.epfl.sdp.cook4me.ui.WelcomeViewModel

/**
 * enum values that represent the screens in the app
 */
enum class Cook4MeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Profile(title = R.string.profile),
}

@Composable
fun Cook4MeApp(
    viewModel: WelcomeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

   NavHost(navController = navController, startDestination = Cook4MeScreen.Start.name) {
       composable(route = Cook4MeScreen.Start.name) {
           WelcomeScreen(onStartButtonClicked = {
               viewModel.setName(it)
               navController.navigate(Cook4MeScreen.Profile.name)} )
       }
       composable(route = Cook4MeScreen.Profile.name) {
           ProfileScreen(uiState.name)
       }
   }
}