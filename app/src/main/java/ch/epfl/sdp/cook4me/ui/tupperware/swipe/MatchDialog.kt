package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.runtime.Composable

// TODO: https://github.com/cook4me/android/issues/181
@Composable
fun MatchDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "It's a match")
        },
        text = {
            Column {
                Text("Here is a text ")
                CircleButton(onClick = { /*TODO*/ }, icon = Icons.Rounded.Chat)
            }
        },
        buttons = {}
    )
}
