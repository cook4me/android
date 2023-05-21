package ch.epfl.sdp.cook4me.ui.navigation

import AddProfileInfoScreen
import SignUpScreen
import VoteWrapper
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
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.challengedetailed.ChallengeDetailedScreen
import ch.epfl.sdp.cook4me.ui.challengefeed.ChallengeFeedScreen
import ch.epfl.sdp.cook4me.ui.challengefeed.FilterScreen
import ch.epfl.sdp.cook4me.ui.challengefeed.SearchViewModel
import ch.epfl.sdp.cook4me.ui.challengeform.CreateChallengeScreen
import ch.epfl.sdp.cook4me.ui.chat.ChannelScreen
import ch.epfl.sdp.cook4me.ui.detailedevent.DetailedEventScreen
import ch.epfl.sdp.cook4me.ui.eventform.CreateEventScreen
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.MapPermissionWrapper
import ch.epfl.sdp.cook4me.ui.profile.EditProfileScreen
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.recipeFeed.RecipeFeed
import ch.epfl.sdp.cook4me.ui.recipeform.CreateRecipeScreen
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import ch.epfl.sdp.cook4me.ui.tupperwareform.CreateTupperwarePermissionWrapper
import ch.epfl.sdp.cook4me.ui.tupperwareswipe.TupperwareSwipeScreen

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
    val searchViewModel = remember { SearchViewModel() }
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
        // the uid of event is predefined on firestore. this is just for show.
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
        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen(
                onCancelListener = { navController.navigate(Screen.ProfileScreen.name) },
                onSuccessListener = { navController.navigate(Screen.ProfileScreen.name) },
            )
        }
        composable(route = Screen.ChatScreen.name) {
            ChannelScreen(
                onBackListener = { navController.navigate(Screen.RecipeFeed.name) },
            )
        }
        composable(route = Screen.CreateChallengeScreen.name) {
            CreateChallengeScreen(
                onCancelClick = { navController.navigateUp() },
                onDoneClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.ChallengeFeedScreen.name) {
            ChallengeFeedScreen(
                onChallengeClick = { challengeId ->
                    navController.navigate(
                        ScreenWithArgs.DetailedChallengeScreen.createRoute(challengeId)
                    )
                },
                onCreateNewChallengeClick = { navController.navigate(Screen.CreateChallengeScreen.name) },
                onFilterClick = { navController.navigate(route = Screen.FilterScreen.name) },
                searchViewModel = searchViewModel
            )
        }
        composable(route = Screen.FilterScreen.name) {
            FilterScreen(
                onCancelClick = { navController.navigateUp() },
                onDoneClick = { navController.navigateUp() },
                viewModel = searchViewModel
            )
        }
        composable(
            route = ScreenWithArgs.DetailedChallengeScreen.name,
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            ChallengeDetailedScreen(
                challengeId = backStackEntry.arguments?.getString("challengeId").orEmpty(),
                onVote = { challengeId ->
                    navController.navigate(
                        ScreenWithArgs.ChallengeVotingScreen.createRoute(challengeId)
                    )
                },
            )
        }
        composable(
            route = ScreenWithArgs.ChallengeVotingScreen.name,
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            VoteWrapper(
                challengeId = backStackEntry.arguments?.getString("challengeId").orEmpty(),
                onBack = { navController.navigateUp() },
                currentUser = "daniel.bucher@epfl.ch"
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
    FilterScreen,
}
