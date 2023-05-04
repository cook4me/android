package ch.epfl.sdp.cook4me.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(navigateTo: (String) -> Unit = {}, currentRoute: String) {
    var expanded by remember { mutableStateOf(false) }

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {
        mainDestinations.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.weight(1f),
                icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navigateTo(screen.route)
                }
            )
        }
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            val moreIcon = painterResource(id = R.drawable.baseline_more_horiz_24)
            BottomNavigationItem(
                modifier = Modifier.fillMaxWidth(),
                icon = { Icon(moreIcon, contentDescription = null) },
                label = { Text(stringResource(R.string.bottom_bar_more_button_text)) },
                selected = expanded,
                onClick = {},
            )
            ExposedDropdownMenu(
                modifier = Modifier.exposedDropdownSize(false).width(120.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dropDownMenuDestinations.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            navigateTo(item.route)
                        },
                        content = { Text(text = item.title) },
                    )
                }
            }
        }
    }
}

sealed class BottomNavScreen(val route: String, val icon: ImageVector?, val title: String) {
    object Tupperwares :
        BottomNavScreen(Screen.TupperwareSwipeScreen.name, Icons.Filled.Home, "Tups")

    object Events : BottomNavScreen(Screen.Event.name, Icons.Filled.Star, "Events")
    object Recipes : BottomNavScreen(Screen.RecipeFeed.name, Icons.Filled.List, "Recipes")
    object Profile : BottomNavScreen(Screen.ProfileScreen.name, Icons.Filled.Person, "Profile")
    object Menu : BottomNavScreen(Screen.RecipeFeed.name, Icons.Filled.Menu, "Menu")
    object MyTupperwares : BottomNavScreen(Screen.RecipeFeed.name, null, "My Tups")
    object MyRecipes : BottomNavScreen(Screen.RecipeFeed.name, null, "My Recipes")
    object MyEvents : BottomNavScreen(Screen.RecipeFeed.name, null, "My Events")
}

val mainDestinations = listOf(
    BottomNavScreen.Tupperwares,
    BottomNavScreen.Recipes,
    BottomNavScreen.Events,
)

val dropDownMenuDestinations = listOf(
    BottomNavScreen.MyTupperwares,
    BottomNavScreen.MyRecipes,
    BottomNavScreen.MyEvents,
    BottomNavScreen.Profile,
)
