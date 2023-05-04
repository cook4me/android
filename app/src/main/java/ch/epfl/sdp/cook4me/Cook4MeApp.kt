package ch.epfl.sdp.cook4me

import AddProfileInfoScreen
import SignUpScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import ch.epfl.sdp.cook4me.permissions.ComposePermissionStatusProvider
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.chat.ChannelScreen
import ch.epfl.sdp.cook4me.ui.detailedevent.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.MapPermissionWrapper
import ch.epfl.sdp.cook4me.ui.navigation.BottomNavigationBar
import ch.epfl.sdp.cook4me.ui.navigation.mainDestinations
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.recipeFeed.RecipeFeed
import ch.epfl.sdp.cook4me.ui.recipeform.CreateRecipeScreen
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwarePermissionWrapper
import ch.epfl.sdp.cook4me.ui.tupperwareswipe.TupperwareSwipeScreen
import com.google.firebase.auth.FirebaseAuth
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
    Event,
    CreateEventScreen,
    DetailedEventScreen,
    SignUpScreen,
    ChatScreen,
    SignUpUserInfos,
    RecipeFeed,
}

/* Testing around the Detailed Event Screen */
// initializing the testing event
val calendar = Calendar.getInstance()

sealed class BottomNavScreen(val route: String, val icon: ImageVector?, val title: String) {
    object Tupperwares :
        BottomNavScreen(Screen.TupperwareSwipeScreen.name, Icons.Filled.Home, "Tups")

    object Events : BottomNavScreen(Screen.Event.name, Icons.Filled.Star, "Events")
    object Recipes : BottomNavScreen(Screen.RecipeFeed.name, Icons.Filled.List, "Recipes")
    object Profile : BottomNavScreen(Screen.ProfileScreen.name, Icons.Filled.Person, "Profile")
    object MyTupperwares : BottomNavScreen(Screen.RecipeFeed.name, null, "My Tups")
    object MyRecipes : BottomNavScreen(Screen.RecipeFeed.name, null, "My Recipes")
    object MyEvents : BottomNavScreen(Screen.RecipeFeed.name, null, "My Events")
    object Chat : BottomNavScreen(Screen.ChatScreen.name, Icons.Filled.Chat, "Chat")
}

@Composable
fun Cook4MeApp(
    permissionStatusProvider: PermissionStatusProvider = ComposePermissionStatusProvider(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA
        )
    )
) {
    // initialize the view model for the sign up screen
    val singUpViewModel = remember { SignUpViewModel() }
    // initialize the auth object for authentication matters
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isAuthenticated = remember { mutableStateOf(auth.currentUser != null) }

    // Navigation controller
    val navController = rememberNavController()

    val startScreen: String = if (isAuthenticated.value) {
        BottomNavScreen.Recipes.route
    } else {
        Screen.Login.name
    }

    val screensWithBottomBar = mainDestinations.map { it.route }
    val shouldShowBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in screensWithBottomBar

    val navGraph = navController.createGraph(startDestination = startScreen) {
        composable(BottomNavScreen.Tupperwares.route) {
            TupperwareSwipeScreen(
                onCreateNewTupperware = { navController.navigate(Screen.CreateTupperwareScreen.name) }
            )
        }
        composable(BottomNavScreen.Events.route) {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                onCreateNewEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) },
            )
        }
        composable(BottomNavScreen.Profile.route) { ProfileScreen() }
        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen(
                onCancelListener = { navController.navigate(Screen.ProfileScreen.name) },
                onSuccessListener = { navController.navigate(Screen.ProfileScreen.name) },
            )
        }
        composable(route = Screen.CreateTupperwareScreen.name) {
            CreateTupperwarePermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                onCancel = {
                    navController.navigate(Screen.TupperwareSwipeScreen.name)
                },
                onSuccessfulSubmit = {
                    navController.navigate(Screen.TupperwareSwipeScreen.name)
                }
            )
        }
        composable(route = Screen.CreateEventScreen.name) { CreateEventScreen() }
        // the uid of event is predefined on firestore. this is just for show.
        composable(route = Screen.DetailedEventScreen.name) { DetailedEventScreen("pSkhty73UrT4f55lIOov") }
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen(
                onSuccessfulSignUp = { navController.navigate(Screen.SignUpUserInfos.name) },
                viewModel = singUpViewModel,
            )
        }
        composable(route = Screen.SignUpUserInfos.name) {
            AddProfileInfoScreen(
                viewModel = singUpViewModel,
                onSuccessfulSignUp = {
                    navController.navigate(
                        startScreen
                    )
                },
                onSignUpFailure = { navController.navigate(Screen.SignUpScreen.name) }
            )
        }
        composable(route = Screen.CreateRecipeScreen.name) {
            CreateRecipeScreen(
                onSuccessfulSubmit = { navController.navigateUp() },
                onCancelClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.RecipeFeed.name) {
            RecipeFeed(
                onCreateNewRecipe = { navController.navigate(Screen.CreateRecipeScreen.name) }
            )
        }
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = {
                    isAuthenticated.value = true
                    navController.navigate(startScreen) {
                        // This popUp blocks the user being able to go back once logged in
                        popUpTo(Screen.Login.name) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.ChatScreen.name) {
            ChannelScreen(
                onBackListener = { navController.navigate(Screen.OverviewScreen.name) },
            )
        }
    }

    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when navigating back
            restoreState = true
        }
    }

    fun signOut() {
        auth.signOut()
        isAuthenticated.value = false
        navController.navigate(Screen.Login.name)
    }

    if (isAuthenticated.value) {
        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar) {
                    BottomNavigationBar(
                        navigateTo = navigateTo,
                        currentRoute = navController
                            .currentBackStackEntryAsState().value?.destination?.route.orEmpty(),
                        onClickSignOut = { signOut() }
                    )
                }
            }
        ) { scaffoldPadding ->
            NavHost(
                navController = navController,
                graph = navGraph,
                modifier = Modifier.padding(scaffoldPadding)
            )
        }
    } else {
        NavHost(navController = navController, graph = navGraph)
    }
}
