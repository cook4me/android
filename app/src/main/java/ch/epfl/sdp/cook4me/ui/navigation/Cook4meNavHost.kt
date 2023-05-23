package ch.epfl.sdp.cook4me.ui.navigation

import AddProfileInfoScreen
import SignUpScreen
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.epfl.sdp.cook4me.application.RecipeFeedService
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.challenge.ChallengeFeedScreen
import ch.epfl.sdp.cook4me.ui.challenge.form.CreateChallengeScreen
import ch.epfl.sdp.cook4me.ui.chat.ChannelScreen
import ch.epfl.sdp.cook4me.ui.event.details.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.event.form.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.map.MapPermissionWrapper
import ch.epfl.sdp.cook4me.ui.recipe.CreateRecipeScreen
import ch.epfl.sdp.cook4me.ui.recipe.feed.RecipeFeed
import ch.epfl.sdp.cook4me.ui.tupperware.form.CreateTupperwarePermissionWrapper
import ch.epfl.sdp.cook4me.ui.tupperware.swipe.TupperwareSwipeScreen
import ch.epfl.sdp.cook4me.ui.user.LoginScreen
import ch.epfl.sdp.cook4me.ui.user.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.user.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.user.profile.ProfileViewModel
import ch.epfl.sdp.cook4me.ui.user.signup.SignUpViewModel

@Composable
fun Cook4MeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.RecipeFeed.name,
    permissionProvider: PermissionStatusProvider,
    onSuccessfulAuth: () -> Unit,
    isOnline: Boolean
) {
    val signUpViewModel = remember { SignUpViewModel() }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.TupperwareSwipeScreen.name) {
            BackHandler(true) {
                // the back button functionality interferes with the swipe screen,
                // therefore we disable it's functionality
            }
            TupperwareSwipeScreen(
                onCreateNewTupperware = { navController.navigate(Screen.CreateTupperwareScreen.name) },
                isOnline = isOnline
            )
        }
        composable(route = Screen.CreateTupperwareScreen.name) {
            CreateTupperwarePermissionWrapper(
                permissionStatusProvider = permissionProvider,
                onCancel = {
                    navController.navigate(Screen.TupperwareSwipeScreen.name)
                },
                onSuccessfulSubmit = {
                    navController.navigate(Screen.TupperwareSwipeScreen.name)
                },
            )
        }
        composable(route = Screen.Event.name) {
            MapPermissionWrapper(
                permissionStatusProvider = permissionProvider,
                onCreateNewEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
                onDetailedEventClick = { navController.navigate(Screen.DetailedEventScreen.name) },
                isOnline = isOnline
            )
        }
        composable(route = Screen.CreateEventScreen.name) {
            CreateEventScreen(
                onCancelClick = { navController.navigateUp() }
            )
        }
        composable(
            route = ScreenWithArgs.DetailedEventScreen.name,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailedEventScreen(backStackEntry.arguments?.getString("eventId").orEmpty())
        }
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen(
                onSuccessfulSignUp = { navController.navigate(Screen.SignUpUserInfo.name) },
                viewModel = signUpViewModel, // TODO Might need some additional changes
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
                service = RecipeFeedService(isOnline = isOnline),
                onCreateNewRecipe = { navController.navigate(Screen.CreateRecipeScreen.name) },
                isOnline = isOnline
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
        // changing the profile screen to take an id as an argument(email)
        // if nothing is passed, it will show the current user's profile
        composable(
            route = "${Screen.ProfileScreen.name}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let {
                ProfileScreen(profileViewModel = ProfileViewModel(id = it))
            } ?: ProfileScreen()
        }

        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen(
                onCancelListener = { navController.navigate(Screen.ProfileScreen.name) },
                onSuccessListener = { navController.navigate(Screen.ProfileScreen.name) },
            )
        }
        composable(route = Screen.ChatScreen.name) {
            ChannelScreen(
                onBackListener = { navController.navigate(Screen.RecipeFeed.name) },
                navController = navController
            )
        }
        composable(route = Screen.CreateChallengeScreen.name) {
            CreateChallengeScreen(
                onCancelClick = {
                    navController.navigate(
                        Screen.ChallengeFeedScreen.name
                    )
                }
            )
        }
        composable(route = Screen.ChallengeFeedScreen.name) {
            ChallengeFeedScreen(
                onCreateNewChallengeClick = { navController.navigate(Screen.CreateChallengeScreen.name) }
            )
        }
    }
}

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
    ChatScreen,
    ChallengeFeedScreen,
    CreateChallengeScreen,
}
