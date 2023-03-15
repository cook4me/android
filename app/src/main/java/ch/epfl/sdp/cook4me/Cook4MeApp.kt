package ch.epfl.sdp.cook4me

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.LoginScreen
import ch.epfl.sdp.cook4me.ui.SwipeScreen

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Login,
    SwipeScreen
}

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Login.name) {
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = { navController.navigate(Screen.SwipeScreen.name) }
            )
        }
        composable(route = Screen.SwipeScreen.name) {
            SwipeScreen()
        }
    }
}
