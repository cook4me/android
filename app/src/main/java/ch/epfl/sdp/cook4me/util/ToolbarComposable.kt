package ch.epfl.sdp.cook4me.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
