package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat.startActivity
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import kotlinx.coroutines.runBlocking

// Refactor needed: https://github.com/cook4me/android/issues/155
@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
) {
    // disconnecting the client before connecting again, otherwise will
    // cause error: too many connections
    runBlocking {
        client.disconnect(true).enqueue()
    }
    val context = LocalContext.current
    val userEmail = accountService.getCurrentUserEmail()
    val fullName = remember { mutableStateOf("") }
    val isConnected = remember { mutableStateOf(false) }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }
    // The user email is always not null, it's a bit of boilerplate.
    userEmail?.let { email ->
        // parsing email to get the name (user id)
        val nameParts = email.split("@")[0].replace(".", "")
        fullName.value = nameParts.trim()
        user.value = User(id = fullName.value)
        // generating user token and connecting the user
        val token = client.devToken(user.value.id)
        client.connectUser(user.value, token).enqueue { result ->
            if (result.isSuccess) {
                isConnected.value = true
            } else {
                println("connection not successful")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(context.getString(R.string.Channel_Screen_Tag))
    ) {
        if (isConnected.value) {
            ChatTheme {
                ChannelsScreen(
                    filters = Filters.and(
                        Filters.eq("type", "messaging"),
                        Filters.`in`("members", listOf(user.value.id)),
                    ),
                    title = "Channel List of ${fullName.value}",
                    isShowingSearch = true,
                    // When clicking on a channel in the channel list, open up
                    // the corresponding message screen
                    onItemClick = { channel ->
                        val intent = MessagesActivity.getIntent(context, channelId = channel.cid)
                        startActivity(context, intent, null)
                    },
                    onBackPressed = { onBackListener() },
                    onHeaderAvatarClick = {
                        client.disconnect(true).enqueue()
                    },
                )
            }
        } else {
            LoadingScreen()
        }
    }
}
