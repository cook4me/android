package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChatScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
) {
    val userEmail = accountService.getCurrentUserEmail()
    userEmail?.let { email ->
        val user = User(email)
        client.updateUser(user).enqueue {
            val user = it.data()
        }
        client.connectUser(user, client.devToken(email)).enqueue()
    }

    ChatTheme {
        ChannelsScreen(
            title = "chat screen!!!!",
            isShowingSearch = true,
            onItemClick = {channel ->  },
            onBackPressed = {}
        )
    }
}