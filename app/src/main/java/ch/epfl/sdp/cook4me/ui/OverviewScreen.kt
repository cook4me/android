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

@Composable
fun OverviewScreen(
    onMapClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onAddTupperwareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth().fillMaxHeight()

    ) {
        Button(onClick = onMapClick) {
            Text("Map")
        }
        Button(onClick = onProfileClick) {
            Text("Profile")
        }
        Button(onClick = onEditProfileClick) {
            Text("Edit Profile")
        }
        Button(onClick = onAddTupperwareClick) {
            Text("Add tupperware")
        }
    }
}
