package ch.epfl.sdp.cook4me

import SignUpScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.persistence.model.Post
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.Event
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import ch.epfl.sdp.cook4me.ui.overview.OverviewScreen
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.PostDetails
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
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
                onAddRecipeClick = { navController.navigate(Screen.CreateRecipeScreen.name) },
                signOutNavigation = { navController.navigate(Screen.Login.name) }
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
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen()
        }
        composable(route = Screen.PostDetails.name) {
            val post = Post(1, "Tiramisu", "This is a delicious triamisu or so")
            PostDetails(data = post, painter = painterResource(R.drawable.tiramisu))
        }
    }
}
