package ch.epfl.sdp.cook4me.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ch.epfl.sdp.cook4me.R

@Composable
fun profileScreen(
name: String
) {
Text("${stringResource(R.string.profile_screen_placeholder)} $name")
}
