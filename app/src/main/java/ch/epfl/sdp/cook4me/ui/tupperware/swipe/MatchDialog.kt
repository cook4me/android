package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.BuildConfig
import ch.epfl.sdp.cook4me.ui.chat.createChatWithPairs
import ch.epfl.sdp.cook4me.ui.chat.provideChatClient

// TODO: https://github.com/cook4me/android/issues/181
@Composable
fun MatchDialog(userEmail: String?, otherUserEmail: String, onDismissRequest: () -> Unit, context: Context) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "It's a match")
        },
        text = {
            Column {
                Text("Here is a text ")
                CircleButton(
                    onClick = {
                        userEmail?.let {
                            createChatWithPairs(
                                userEmail,
                                otherUserEmail,
                                client = provideChatClient(
                                    apiKey = BuildConfig.CHAT_API_KEY,
                                    context = context
                                ),
                                context = context
                            )
                        }
                    },
                    icon = Icons.Rounded.Chat,
                    color = MaterialTheme.colors.primary
                )
            }
        },
        buttons = {}
    )
}
