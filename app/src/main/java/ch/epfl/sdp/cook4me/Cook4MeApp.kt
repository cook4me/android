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
import ch.epfl.sdp.cook4me.ui.eventform.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.Event
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwareScreenWithState
import ch.epfl.sdp.cook4me.ui.tupperwareform.TupCreationViewModel
import java.util.Calendar

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
    CreateEventScreen,
    DetailedEventScreen
}

/* Testing around the Detailed Event Screen */
// initializing the testing event
val calendar = Calendar.getInstance()
val testEvent = Event(
    name = "test event name",
    description = "test description",
    dateTime = calendar,
    location = "Rue. Louis Favre 4, 1024, Ecublens",
    maxParticipants = 4,
    participants = listOf("obi.wang", "harry.potter"),
    creator = "peter griffin",
    id = "jabdsfias213",
    isPrivate = false
)

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.OverviewScreen.name) {
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
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) }
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
            CreateTupperwareScreenWithState(TupCreationViewModel())
        }
        composable(route = Screen.CreateEventScreen.name) {
            CreateEventScreen()
        }
        composable(route = Screen.DetailedEventScreen.name) {
            DetailedEventScreen(event = testEvent)
        }
    }
}
