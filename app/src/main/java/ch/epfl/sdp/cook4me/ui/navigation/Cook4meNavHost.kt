package ch.epfl.sdp.cook4me.ui.navigation

import AddProfileInfoScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.persistence.model.Post
import ch.epfl.sdp.cook4me.ui.detailedevent.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.MapPermissionWrapper
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.PostDetails
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.recipeFeed.RecipeFeed
import ch.epfl.sdp.cook4me.ui.recipeform.CreateRecipeScreen
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwarePermissionWrapper
import ch.epfl.sdp.cook4me.ui.tupperwareswipe.TupperwareSwipeScreen


enum class Screen {
    Login,
    CreateTupperwareScreen,
    TupperwareSwipeScreen,
    ProfileScreen,
    CreateRecipeScreen,
    EditProfileScreen,
    Event,
    CreateEventScreen,
    DetailedEventScreen,
    SignUpScreen,
    SignUpUserInfo,
    RecipeFeed,
}

@Composable
fun Cook4MeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "profile",
    permissionStatusProvider: PermissionStatusProvider,
    onSuccessfulAuth: () -> Unit,
) {
    val signUpViewModel = remember { SignUpViewModel()}
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.TupperwareSwipeScreen.name) {
            TupperwareSwipeScreen(
                onCreateNewTupperware = { navController.navigate(Screen.CreateTupperwareScreen.name) }
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
        composable(route = Screen.Event.name) {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                onCreateNewEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) },
            )
        }
        composable(route = Screen.CreateEventScreen.name) { CreateEventScreen() }
        // the uid of event is predefined on firestore. this is just for show.
        composable(route = Screen.DetailedEventScreen.name) { DetailedEventScreen("IcxAvzg7RfckSxw9K5I0") }
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen(
                onSuccessfulSignUp = { navController.navigate(Screen.SignUpUserInfo.name) },
                viewModel = signUpViewModel,      // TODO Might need some additional changes
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
        composable(route = Screen.SignUpUserInfo.name) {
            AddProfileInfoScreen(
                viewModel = signUpViewModel,
                onSuccessfulSignUp = {
                    navController.navigate(
                        startDestination
                    )
                },
                onSignUpFailure = { navController.navigate(Screen.SignUpScreen.name) }
            )
        }
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = {
                    onSuccessfulAuth()
                    navController.navigate(startDestination) {
                        // This popUp blocks the user being able to go back once logged in
                        popUpTo(Screen.Login.name) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.ProfileScreen.name) { ProfileScreen() }
        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen(
                onCancelListener = { navController.navigate(Screen.ProfileScreen.name) },
                onSuccessListener = { navController.navigate(Screen.ProfileScreen.name) },
            )
        }
    }
}