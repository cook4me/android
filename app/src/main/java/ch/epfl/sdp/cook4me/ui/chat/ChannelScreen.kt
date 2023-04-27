package ch.epfl.sdp.cook4me.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
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
    // using the current user email as the user id for the stream chat
    // log in with the user.
    // e.g. email is darth.vadar@epfl.ch then id is darthvadar
    val userEmail = accountService.getCurrentUserEmail()
    val fullName = remember { mutableStateOf("") }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }
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
            } else {
                // Handle result.error()
                println(result.error().message)
            }
        }
    }
    val selectedChannelId = remember { mutableStateOf("") }

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
            // the code is working now because I passed nothing to the
            // onbacklistener in the cook4meapp.kt
            // however if I change it to navController.navigate(Screen.OverviewScreen.name) (or any other navigation)
            // the channel screen no longer displays channels.
            // if i just pass a println, then everything is fine (wtf????)

            // also, if I include navcontroller in the constructor, it also doesn't work
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
                        println(result.error().message)
                    }
                }
            },
        )
        if (selectedChannelId.value.isNotEmpty()) {
            MessageScreen(channelId = selectedChannelId.value, onBackListener = { selectedChannelId.value = "" })
        }
    }
}