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
import ch.epfl.sdp.cook4me.R

@Composable
fun OverviewScreen(
    onMapClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onAddTupperwareClick: () -> Unit,
    onSwipeTupperwareClick: () -> Unit,
    onAddEventClick: () -> Unit,
    onDetailedEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
    }
}
