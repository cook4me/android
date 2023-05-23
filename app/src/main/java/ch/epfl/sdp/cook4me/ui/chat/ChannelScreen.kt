package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import ch.epfl.sdp.cook4me.BuildConfig
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import ch.epfl.sdp.cook4me.ui.navigation.Screen
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors


@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = BuildConfig.CHAT_API_KEY,
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
    navController: NavController,
) {
    val context = LocalContext.current
    val userEmail = accountService.getCurrentUserWithEmail()
    val fullName = remember { mutableStateOf("") }
    val isConnected = remember { mutableStateOf(false) }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }

    userEmail?.let { email ->
        // parsing email to get the name (user id)
        val nameParts = email.split("@")[0].replace(".", "")
        fullName.value = nameParts.trim()
        user.value = User(
            id = fullName.value,
            image = "${PROFILE_IMAGE_PREFIX}$email",
            extraData = mutableMapOf(
                "email" to userEmail
            )
        )
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
            ChatTheme(
                imageLoaderFactory = CoilImageLoaderFactory,
                isInDarkMode = false,
                colors = StreamColors.defaultColors().copy(
                    errorAccent = MaterialTheme.colors.onError,
                    primaryAccent = MaterialTheme.colors.primary,
                    infoAccent = MaterialTheme.colors.secondary
                )
            ) {
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
                        val intent = MessagesActivity.getIntent(context, channelId = channel.cid, userEmail = userEmail)
                        startActivity(context, intent, null)
                    },
                    onBackPressed = { onBackListener() },
                    isShowingHeader = false,
                    onViewChannelInfoAction = { channel ->
                        val targetMember = channel.members.find { it.user.extraData["email"] != userEmail }
                        val targetEmail = targetMember?.user?.extraData?.get("email")
                        if (targetEmail != null) {
                            navController.navigate("${Screen.ProfileScreen.name}/$targetEmail")
                        }
                    }
                )
            }
        } else {
            LoadingScreen()
        }
    }
}
