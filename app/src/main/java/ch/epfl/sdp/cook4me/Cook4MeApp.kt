package ch.epfl.sdp.cook4me

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.GoogleMapView
import ch.epfl.sdp.cook4me.ui.ProfileScreen
import ch.epfl.sdp.cook4me.ui.WelcomeScreen
import ch.epfl.sdp.cook4me.ui.dummyMarkers

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Start,
    Profile,
    CoolMap
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
                },
                onMapButtonClicked = {
                    navController.navigate(Screen.CoolMap.name)
                }
            )
        }
        composable(route = Screen.Profile.name) { ProfileScreen(name) }
        composable(route = Screen.CoolMap.name) { GoogleMapView(modifier = Modifier.fillMaxSize(), markers = dummyMarkers) }
    }
}
