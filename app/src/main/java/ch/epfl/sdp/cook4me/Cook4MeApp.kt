package ch.epfl.sdp.cook4me

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.OverviewScreen
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwareScreenWithState
import ch.epfl.sdp.cook4me.ui.tupperwareform.TupCreationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwareScreen

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Login,
    CreateTupperwareScreen,
    OverviewScreen,
    ProfileScreen,
    EditProfileScreen,
    Map,
    CreateEventScreen
}

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController()
) {
    // initialize the auth object for authentication matters
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // the current logged in user, if no user is logged in, then return null
    val currentUser: FirebaseUser? = auth.currentUser
    // depending on if current user exists, choose different start destination of the app.
    val startScreen: String = if (currentUser != null) {
        // already signed in, switch to overview screen
        Screen.OverviewScreen.name
    } else {
        // not signed in yet, navigate to sign in screen
        Screen.Login.name
    }
    NavHost(navController = navController, startDestination = startScreen) {
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = { navController.navigate(Screen.OverviewScreen.name) }
            )
        }
        composable(route = Screen.OverviewScreen.name) {
            OverviewScreen(
                onMapClick = { navController.navigate(Screen.Map.name) },
                onProfileClick = { navController.navigate(Screen.ProfileScreen.name) },
                onEditProfileClick = { navController.navigate(Screen.EditProfileScreen.name) },
                onAddTupperwareClick = { navController.navigate(Screen.CreateTupperwareScreen.name) },
                onAddEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
            )
        }
        composable(route = Screen.Map.name) {
            GoogleMapView(modifier = Modifier.fillMaxSize(), markers = dummyMarkers)
        }
        composable(route = Screen.ProfileScreen.name) {
            ProfileScreen()
        }
        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen()
        }
        composable(route = Screen.CreateTupperwareScreen.name) {
            CreateTupperwareScreen()
        }
        composable(route = Screen.CreateEventScreen.name) {
            CreateEventScreen()
        }
    }
}
