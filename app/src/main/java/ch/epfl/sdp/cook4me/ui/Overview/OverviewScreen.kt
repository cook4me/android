package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.Overview.OverviewViewModel

@Composable
fun OverviewScreen(
    onMapClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onAddTupperwareClick: () -> Unit,
    onSwipeTupperwareClick: () -> Unit,
    onAddSignUpClick: () -> Unit,
    onAddEventClick: () -> Unit,
    onPostClick: () -> Unit,
    onDetailedEventClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    signOutNavigation: () -> Unit,
    overviewViewModel: OverviewViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Listen to the navigation state and navigate to the correct screen
    val navigationState = overviewViewModel.navigationState
    val signOutErrorMessage = overviewViewModel.signOutErrorMessage
    if(navigationState.value == 1) {
        overviewViewModel.navigationState.value = 0
        signOutNavigation()
    }
    if(signOutErrorMessage.value != null) {
        println(signOutErrorMessage.value)
        overviewViewModel.signOutErrorMessage.value = null
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(stringResource(R.string.Overview_Screen_Tag))
    ) {
        Button(onClick = onMapClick) {
            Text(stringResource(R.string.navigate_to_map))
        }
        Button(onClick = onProfileClick) {
            Text(stringResource(R.string.navigate_to_profile))
        }
        Button(onClick = onEditProfileClick) {
            Text(stringResource(R.string.navigate_to_edit_profile))
        }
        Button(onClick = onAddTupperwareClick) {
            Text(stringResource(R.string.navigate_to_add_tupperware))
        }
        Button(onClick = onAddRecipeClick) {
            Text(stringResource(R.string.navigate_to_add_recipe))
        }
        Button(onClick = onSwipeTupperwareClick) {
            Text(stringResource(R.string.navigate_to_swipe_tupperware))
        }
        Button(onClick = onAddEventClick) {
            Text(stringResource(R.string.navigate_to_add_event))
        }
        Button(onClick = onDetailedEventClick) {
            Text(stringResource(R.string.Nav_Detailed_Event_Screen))
        }
        Button(onClick = onAddSignUpClick) {
            Text(stringResource(R.string.navigate_to_add_signup))
        }
        Button(onClick = onPostClick) {
            Text(stringResource(R.string.navigate_to_postView))
        }
        Button(onClick = { overviewViewModel.signOut() }) {
            Text(stringResource(R.string.sign_out))
        }
    }
}
