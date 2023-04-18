package ch.epfl.sdp.cook4me

import SignUpScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import ch.epfl.sdp.cook4me.persistence.model.Post
import ch.epfl.sdp.cook4me.ui.OverviewScreen
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.Event
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import ch.epfl.sdp.cook4me.ui.navigation.BottomNavigationBar
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.PostDetails
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.recipeform.CreateRecipeScreen
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwareScreen
import ch.epfl.sdp.cook4me.ui.tupperwareswipe.TupperwareSwipeScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Login,
    CreateTupperwareScreen,
    TupperwareSwipeScreen,
    OverviewScreen,
    ProfileScreen,
    CreateRecipeScreen,
    EditProfileScreen,
    Map,
    CreateEventScreen,
    DetailedEventScreen,
    SignUpScreen,
    PostDetails,
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

sealed class BottomNavScreen(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavScreen(Screen.CreateTupperwareScreen.name, Icons.Filled.Home, "Home")
    object Map : BottomNavScreen(Screen.Map.name, Icons.Filled.LocationOn, "Map")
    object Profile : BottomNavScreen(Screen.ProfileScreen.name, Icons.Filled.Person, "Profile")
    object Menu: BottomNavScreen(Screen.OverviewScreen.name, Icons.Filled.Menu, "Menu")
}

@Composable
fun Cook4MeApp() {
    // User authentication
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isAuthenticated = remember { mutableStateOf(auth.currentUser != null) }

    // Navigation controller
    val navController = rememberNavController()

    val startScreen: String = if (isAuthenticated.value) {
        BottomNavScreen.Map.route
    } else {
        Screen.Login.name
    }

    val navGraph = navController.createGraph(startDestination = startScreen) {
        composable(BottomNavScreen.Home.route) { CreateTupperwareScreen() }
        composable(BottomNavScreen.Map.route) { GoogleMapView(modifier = Modifier.fillMaxSize(), markers = dummyMarkers) }
        composable(BottomNavScreen.Profile.route) { ProfileScreen() }
        composable(BottomNavScreen.Menu.route) {
            OverviewScreen(
                onMapClick = { navController.navigate(Screen.Map.name) },
                onProfileClick = { navController.navigate(Screen.ProfileScreen.name) },
                onEditProfileClick = { navController.navigate(Screen.EditProfileScreen.name) },
                onAddTupperwareClick = { navController.navigate(Screen.CreateTupperwareScreen.name) },
                onSwipeTupperwareClick = { navController.navigate(Screen.TupperwareSwipeScreen.name) },
                onAddEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
                onAddSignUpClick = { navController.navigate(Screen.SignUpScreen.name) },
                onPostClick = { navController.navigate(Screen.PostDetails.name) },
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) },
                onAddRecipeClick = { navController.navigate(Screen.CreateRecipeScreen.name) },
            )
        }
        composable(route = Screen.EditProfileScreen.name) { EditProfileScreen() }
        composable(route = Screen.TupperwareSwipeScreen.name) { TupperwareSwipeScreen() }
        composable(route = Screen.CreateEventScreen.name) { CreateEventScreen() }
        composable(route = Screen.DetailedEventScreen.name) { DetailedEventScreen(event = testEvent) }
        composable(route = Screen.SignUpScreen.name) { SignUpScreen() }
        composable(route = Screen.CreateRecipeScreen.name) { CreateRecipeScreen(submitForm = {}) }
        composable(route = Screen.PostDetails.name) {
            val post = Post(1, "Tiramisu", "This is a delicious triamisu or so")
            PostDetails(data = post, painter = painterResource(R.drawable.tiramisu))
        }
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = {
                    isAuthenticated.value = true
                    navController.navigate(BottomNavScreen.Map.route) {
                        popUpTo(Screen.Login.name) { inclusive = true } // This popUp blocks the user being able to go back once logged in
                    }
                }
            )
        }
    }
    if (isAuthenticated.value) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }

        ) { scaffoldPadding ->
            NavHost(navController = navController, graph = navGraph, modifier = Modifier.padding(scaffoldPadding))
        }
    } else {
        NavHost(navController = navController, graph = navGraph)
    }
}

/*
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
                onSwipeTupperwareClick = { navController.navigate(Screen.TupperwareSwipeScreen.name) },
                onAddEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
                onAddSignUpClick = { navController.navigate(Screen.SignUpScreen.name) },
                onPostClick = { navController.navigate(Screen.PostDetails.name) },
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) },
                onAddRecipeClick = { navController.navigate(Screen.CreateRecipeScreen.name) },
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
        composable(route = Screen.TupperwareSwipeScreen.name) {
            TupperwareSwipeScreen()
        }
        composable(route = Screen.CreateEventScreen.name) {
            CreateEventScreen()
        }
        composable(route = Screen.DetailedEventScreen.name) {
            DetailedEventScreen(event = testEvent)
        }
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen()
        }
        composable(route = Screen.PostDetails.name) {
            val post = Post(1, "Tiramisu", "This is a delicious triamisu or so")
            PostDetails(data = post, painter = painterResource(R.drawable.tiramisu))
        }
        composable(route = Screen.CreateRecipeScreen.name) {
            CreateRecipeScreen(submitForm = {})
        }
    }
}
*/
