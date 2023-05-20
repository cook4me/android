package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat.startActivity
import ch.epfl.sdp.cook4me.BuildConfig
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import coil.ComponentRegistry
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import com.getstream.sdk.chat.coil.StreamImageLoaderFactory
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.util.StreamCoilImageLoaderFactory


// TODO: Refactor needed: https://github.com/cook4me/android/issues/155
@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = BuildConfig.CHAT_API_KEY,
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
) {

    val context = LocalContext.current
    val userEmail = accountService.getCurrentUserWithEmail()
    val fullName = remember { mutableStateOf("") }
    val isConnected = remember { mutableStateOf(false) }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }

    // TODO: I would have preferred to use LaunchedEffect instead of a callback chain
    client.disconnect(true).enqueue { disconnectResult ->
        if (disconnectResult.isSuccess) {
            // disconnecting the client before connecting again, otherwise will
            // cause error: too many connections
            // The user email is always not null, it's a bit of boilerplate.
            userEmail?.let { email ->
                // parsing email to get the name (user id)
                val nameParts = email.split("@")[0].replace(".", "")
                fullName.value = nameParts.trim()
                user.value = User(id = fullName.value, image = "${PROFILE_IMAGE_PREFIX}$email")
                // generating user token and connecting the user
                val token = client.devToken(user.value.id)
                client.connectUser(user.value, token).enqueue { result ->
                    if (result.isSuccess) {
                        isConnected.value = true
                    } else {
                        //TODO: use Log.e(...) instead
                        println("connection not successful")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(context.getString(R.string.Channel_Screen_Tag))
    ) {
        if (isConnected.value) {
            ChatTheme(imageLoaderFactory = CoilImageLoaderFactory) {
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
                    isShowingHeader = false
                )
            }
        } else {
            LoadingScreen()
        }
    }
}