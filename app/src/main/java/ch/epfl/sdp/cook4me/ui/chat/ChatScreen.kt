package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ch.epfl.sdp.cook4me.application.provideChatClient
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory



@Composable
fun ChatScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    )
) {
    val user = User(
        id = "tutorial-droid",
        name = "Tutorial Droid",
        image = "https://bit.ly/2TIt8NR"
    )
    client.connectUser(
        user = user,
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
    ).enqueue()

    ChatTheme {
        ChannelsScreen(
            title = "chat screen!!!!",
            isShowingSearch = true,
            onItemClick = {channel ->  },
            onBackPressed = {}
        )
    }
}