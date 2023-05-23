package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import kotlinx.coroutines.coroutineScope

// TODO: https://github.com/cook4me/android/issues/181
@Composable
fun MatchDialog(user: String, otherUser: String, profileImageRepository: ProfileImageRepository = ProfileImageRepository(), onDismissRequest: () -> Unit) {
    coroutineScope {
        val myImage = profileImageRepository.getProfile(user)
        val otherImage = profileImageRepository.getProfile(otherUser)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "It's a match")
        },
        text = {
            Column {
                Text("Here is a text ")
                CircleButton(onClick = { /*TODO*/ }, icon = Icons.Rounded.Chat, color = MaterialTheme.colors.primary)
            }
        },
        buttons = {}
    )
}
