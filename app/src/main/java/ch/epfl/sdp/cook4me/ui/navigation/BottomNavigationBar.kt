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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(
    navigateTo: (String) -> Unit = {},
    currentRoute: String,
    onClickSignOut: () -> Unit,
    isOnline: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {
        mainDestinations.filterNot { screen -> !isOnline && screen.title === "Chat" }.forEach { screen ->
            val label: @Composable (() -> Unit)? =
                if (currentRoute == screen.route) { { Text(screen.title) } } else null

            BottomNavigationItem(
                modifier = Modifier.weight(1f),
                icon = { screen.icon?.let { Icon(painterResource(id = it), contentDescription = null) } },
                label = label,
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
                modifier = Modifier
                    .exposedDropdownSize(false)
                    .width(120.dp),
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
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClickSignOut()
                    },
                    content = { Text(text = stringResource(R.string.bottom_bar_sign_out_text)) },
                )
            }
        }
    }
}

sealed class BottomNavScreen(val route: String, val icon: Int?, val title: String) {
    object Tupperwares :
        BottomNavScreen(Screen.TupperwareSwipeScreen.name, R.drawable.baseline_tupperware_24, "Tups")

    object Events : BottomNavScreen(Screen.Event.name, R.drawable.baseline_event_24, "Events")
    object Recipes : BottomNavScreen(Screen.RecipeFeed.name, R.drawable.baseline_menu_book_24, "Recipes")
    object Profile : BottomNavScreen(Screen.ProfileScreen.name, null, "Profile")
    object Chat : BottomNavScreen(Screen.ChatScreen.name, R.drawable.baseline_chat_24, "Chat")
    object Challenges :
        BottomNavScreen(Screen.ChallengeFeedScreen.name, R.drawable.baseline_challenge_24, "Contest")
}

sealed class ScreenWithArgs(val name: String) {
    object DetailedEventScreen : ScreenWithArgs("detailed_event_screen/{eventId}") {
        fun createRoute(eventId: String) = "detailed_event_screen/$eventId"
    }
    object DetailedChallengeScreen : ScreenWithArgs("detailed_challenge_screen/{challengeId}") {
        fun createRoute(challengeId: String) = "detailed_challenge_screen/$challengeId"
    }
    object ChallengeVotingScreen : ScreenWithArgs("challenge_voting_screen/{challengeId}") {
        fun createRoute(challengeId: String) = "challenge_voting_screen/$challengeId"
    }
}

val mainDestinations = listOf(
    BottomNavScreen.Tupperwares,
    BottomNavScreen.Recipes,
    BottomNavScreen.Events,
    BottomNavScreen.Challenges,
    BottomNavScreen.Chat
)

val dropDownMenuDestinations = listOf(
    BottomNavScreen.Profile
)
