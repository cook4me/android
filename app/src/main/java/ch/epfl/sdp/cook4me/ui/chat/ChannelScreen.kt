package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
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
    val context = LocalContext.current
    val userEmail = accountService.getCurrentUserEmail()
    val fullName = remember { mutableStateOf("") }
//    val selectedChannelId = remember { mutableStateOf("") }
    val isConnected = remember { mutableStateOf(false) }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }
    userEmail?.let { email ->
        val nameParts = email.split("@")[0].replace(".", "")
        fullName.value = nameParts.trim()
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

    Box(modifier = Modifier.fillMaxSize()) {
        if (isConnected.value) {
            ChatTheme {
                ChannelsScreen(
                    filters = Filters.and(
                        Filters.eq("type", "messaging"),
                        Filters.`in`("members", listOf(user.value.id)),
                    ),
                    title = "Channel List of ${fullName.value}",
                    isShowingSearch = true,
                    /*onItemClick = { channel ->
                        selectedChannelId.value = channel.cid
                    },*/
                    onItemClick = { channel ->
                        val intent = MessagesActivity.getIntent(context, channelId = channel.cid)
                        startActivity(context, intent, null)
                    },

                    onBackPressed = { onBackListener() },
                    onHeaderAvatarClick = {
                        client.disconnect(true).enqueue()
                    },
                    onHeaderActionClick = {
                        client.createChannel(
                            channelType = "messaging",
                            channelId = "",
                            memberIds = listOf(fullName.value, "notexistinguser"),
                            extraData = emptyMap()
                        ).enqueue { result ->
                            if (result.isSuccess) {
                                val channel = result.data()
                                // selectedChannelId.value = channel.cid
                                val intent = MessagesActivity.getIntent(context, channelId = channel.cid)
                                startActivity(context, intent, null)
                            } else {
                                // println(result.error().message) <- detekt is not happy with this
                                println("create channel failed")
                            }
                        }
                    },
                )
                /*if (selectedChannelId.value.isNotEmpty()) {
                    MessageScreen(
                        channelId = selectedChannelId.value,
                        onBackListener = { selectedChannelId.value = "" },
                    )
                }*/
            }
        } else {
            LoadingScreen()
        }
    }
}
