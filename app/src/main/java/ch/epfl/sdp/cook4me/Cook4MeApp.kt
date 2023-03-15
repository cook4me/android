package ch.epfl.sdp.cook4me

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.LoginScreen
import ch.epfl.sdp.cook4me.ui.SwipeScreen
import ch.epfl.sdp.cook4me.ui.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Login,
    TupCreationScreen,
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
        composable(route = Screen.TupCreationScreen.name) {
            TupCreationScreenWithState(TupCreationViewModel())
        }
        composable(route = Screen.SwipeScreen.name) {
            SwipeScreen()
        }
    }
}
