package ch.epfl.sdp.cook4me.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R

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
    onAddRecipeClick: () -> Unit,
    onRecipeFeedClick: () -> Unit,
    signOutNavigation: () -> Unit,
    onDetailedEventClick: () -> Unit,
    overviewViewModel: OverviewViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Listen to the navigation state and navigate to the correct screen
    val navigationState = overviewViewModel.navigationState
    if (navigationState.value == 1) {
        overviewViewModel.navigationState.value = 0
        signOutNavigation()
    }
    // Collect user email from viewModel
    val userEmail = overviewViewModel.userEmail.collectAsState()

    Box(
        modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.Overview_Screen_Tag))
    ) {
        Text(
            text = stringResource(R.string.Current_user_header) + userEmail.value,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            fontWeight = FontWeight.Bold
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
            Button(onClick = onRecipeFeedClick) {
                Text(stringResource(R.string.navigate_to_recipe_feed))
            }
            Button(onClick = { overviewViewModel.onSignOutButtonClicked() }) {
                Text(stringResource(R.string.sign_out))
            }
        }
    }
}

@Composable
fun CurrentLoggedInEmailText(email: String?) {
    var userEmail = email
    if (userEmail == null) {
        userEmail = stringResource(R.string.Empty_User_Email)
    }
    Text(
        text = stringResource(R.string.Current_user_header) + userEmail,
        modifier = Modifier
            .padding(16.dp),
        fontWeight = FontWeight.Bold
    )
}
