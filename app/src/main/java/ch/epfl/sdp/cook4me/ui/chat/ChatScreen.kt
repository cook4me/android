package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val offlinePluginFactory = StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
        ),
        appContext = context,
    )
    val client = ChatClient.Builder("b67pax5b2wdq", appContext = context)
        .withPlugin(offlinePluginFactory)
        .logLevel(ChatLogLevel.ALL)
        .build()
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
        )
    }
}