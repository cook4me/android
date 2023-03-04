package ch.epfl.sdp.cook4me

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.ProfileScreen
import ch.epfl.sdp.cook4me.ui.SignInScreen
import ch.epfl.sdp.cook4me.ui.WelcomeScreen

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Start,
    Profile
}

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController()
) {
    var name by remember {
        mutableStateOf("")
    }

    NavHost(navController = navController, startDestination = Screen.Start.name) {
        composable(route = Screen.Start.name) {
            WelcomeScreen(
                onStartButtonClicked = {
                    name = it
                    navController.navigate(Screen.Profile.name)
                }
            )
        }
        composable(route = Screen.Profile.name) {
            ProfileScreen(name)
        }
    }
}
