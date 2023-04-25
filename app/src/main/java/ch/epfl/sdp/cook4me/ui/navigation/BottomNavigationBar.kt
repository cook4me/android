package ch.epfl.sdp.cook4me.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.BottomNavScreen

val mainScreens = listOf(
    BottomNavScreen.Tupperwares,
    BottomNavScreen.Recipes,
    BottomNavScreen.Events,
    BottomNavScreen.Profile,
    BottomNavScreen.Menu
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomNavigationBar(navController: NavHostController = rememberNavController()) {
    val screens = listOf(
        BottomNavScreen.Tupperwares,
        BottomNavScreen.Recipes,
        BottomNavScreen.Events,
        BottomNavScreen.Profile,
        BottomNavScreen.Menu
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
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
            )
        }
    }
}
