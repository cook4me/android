package ch.epfl.sdp.cook4me

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.permissions.ComposePermissionStatusProvider
import ch.epfl.sdp.cook4me.permissions.NetworkCallback
import ch.epfl.sdp.cook4me.permissions.PermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.navigation.BottomNavigationBar
import ch.epfl.sdp.cook4me.ui.navigation.Cook4MeNavHost
import ch.epfl.sdp.cook4me.ui.navigation.Screen
import ch.epfl.sdp.cook4me.ui.navigation.mainDestinations
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

/* Testing around the Detailed Event Screen */
// initializing the testing event
val calendar: Calendar = Calendar.getInstance()
val unauthenticatedStartScreen = Screen.Login.name
val authenticatedStartScreen = Screen.RecipeFeed.name

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController(),
    permissionStatusProvider: PermissionStatusProvider = ComposePermissionStatusProvider(
        listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    )
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isAuthenticated = remember { mutableStateOf(auth.currentUser != null) }
    val startScreen = remember {
        mutableStateOf(
            if (!isAuthenticated.value) unauthenticatedStartScreen else authenticatedStartScreen
        )
    }

    val connectivityManager = LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = remember {
        NetworkCallback()
    }

    DisposableEffect(connectivityManager, networkCallback) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    val isOnline = networkCallback.isOnline.value

    val screensWithBottomBar = mainDestinations.map { it.route }
    val shouldShowBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in screensWithBottomBar
    val currentRoute = { navController.currentDestination?.route }

    fun navigateToBottomBarRoute(route: String) {
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

    fun onSuccessfulAuth() {
        isAuthenticated.value = true
        startScreen.value = Screen.RecipeFeed.name
    }

    fun signOut() {
        auth.signOut()
        isAuthenticated.value = false
        navController.navigate(Screen.Login.name)
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    navigateTo = { route -> navigateToBottomBarRoute(route) },
                    currentRoute = currentRoute().orEmpty(),
                    onClickSignOut = { signOut() }
                )
            }
        }
    ) { scaffoldPadding ->
        Cook4MeNavHost(
            modifier = Modifier.padding(scaffoldPadding),
            navController = navController,
            startDestination = startScreen.value,
            permissionProvider = permissionStatusProvider,
            onSuccessfulAuth = { onSuccessfulAuth() },
            isOnline = isOnline
        )
    }
}
