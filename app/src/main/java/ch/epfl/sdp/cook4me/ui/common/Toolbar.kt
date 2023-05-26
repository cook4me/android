package ch.epfl.sdp.cook4me.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        title = {
            Text(title, Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        },
    )
}
