package ch.epfl.sdp.cook4me.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID


@SuppressLint("UnrememberedMutableState")
@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
) {
    val channelClient = client.channel(channelType = "messaging", channelId = "general")

    // using the current user email as the user id for the stream chat
    // log in with the user.
    // e.g. email is darth.vadar@epfl.ch then id is darth-vadar
    val userEmail = accountService.getCurrentUserEmail()
    userEmail?.let { email ->
        val nameParts = email.split("@")[0].split(".")
        val firstName = nameParts[0].trim()
        val secondName = nameParts[1].trim()
        val fullName = "$firstName-$secondName"
        val user = User(
            id = fullName,
            image = "https://picsum.photos/200"
        )
        val token = client.devToken(user.id)
        client.connectUser(user, token).enqueue { result ->
            if (result.isSuccess) {
                // Connected
            } else {
                // Handle result.error()
                println(result.error().message)
            }
        }
    }

    ChatTheme {
        ChannelsScreen(
            title = "Channel List",
            isShowingSearch = true,
            onItemClick = { channel ->
                /*TODO*/
            },
            onBackPressed = { onBackListener() },
            onHeaderActionClick = {
                // just creating a channel of 2 ppl, the current user and the sdp2023cook4me user (already manually registered)
                client.createChannel(
                    channelType = "messaging",
                    channelId = "",
                    memberIds = listOf("daniel-bucher", "sdp2023cook4me"),
                    extraData = emptyMap()
                ).enqueue { result ->
                    if (result.isSuccess) {
                        val channel = result.data()
                    } else {
                        // Handle result.error()
                        println(result.error().message)
                    }
                }
            },
        )
    }
}