package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
) {
    val userEmail = accountService.getCurrentUserEmail()
    val fullName = remember { mutableStateOf("") }
    val selectedChannelId = remember { mutableStateOf("") }
    val isConnected = remember { mutableStateOf(false) }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }
    // client.disconnect(true).enqueue()
    userEmail?.let { email ->
        val nameParts = email.split("@")[0].split(".")
        val firstName = nameParts[0].trim()
        val secondName = nameParts[1].trim()
        fullName.value = "$firstName$secondName"
        user.value = User(id = fullName.value)
        val token = client.devToken(user.value.id)
        client.connectUser(user.value, token).enqueue { result ->
            if (result.isSuccess) {
                // Connected
                isConnected.value = true
            } else {
                // println(result.error().message) <--- detekt is not happy with this
                println("connection not successful")
            }
        }
    }

    Box {
        if (isConnected.value) {
            ChatTheme {
                ChannelsScreen(
                    filters = Filters.and(
                        Filters.eq("type", "messaging"),
                        Filters.`in`("members", listOf(user.value.id)),
                    ),
                    title = "Channel List of ${fullName.value}",
                    isShowingSearch = true,
                    onItemClick = { channel ->
                        selectedChannelId.value = channel.cid
                    },
                    onBackPressed = { onBackListener() },
                    onHeaderAvatarClick = {
                        client.disconnect(true).enqueue()
                    },
                    onHeaderActionClick = {
                        // just creating a channel of 2 ppl
                        client.createChannel(
                            channelType = "messaging",
                            channelId = "",
                            memberIds = listOf(fullName.value, "danielbucher"),
                            extraData = emptyMap()
                        ).enqueue { result ->
                            if (result.isSuccess) {
                                val channel = result.data()
                                selectedChannelId.value = channel.cid
                            } else {
                                // println(result.error().message) <- detekt is not happy with this
                                println("create channel failed")
                            }
                        }
                    },
                )
                if (selectedChannelId.value.isNotEmpty()) {
                    MessageScreen(
                        channelId = selectedChannelId.value,
                        onBackListener = { selectedChannelId.value = "" },
                    )
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
