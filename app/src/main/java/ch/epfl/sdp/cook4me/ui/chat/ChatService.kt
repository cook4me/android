package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import androidx.core.content.ContextCompat
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User

// create a chat with the given email and go to the corresponding message screen
/*
* @params:
* @targetEmail: the email of the user who is the current user is going to
* start a chat with.
*
* @client: the chat client, refer to overviewScreen or ChannelScreen for a look.
* */
fun createChatWithEmail(
    targetEmail: String,
    accountService: AccountService = AccountService(),
    client: ChatClient,
    context: Context,
) {
    val userEmail = accountService.getCurrentUserEmail()
    userEmail?.let {
        // parsing email to get the name (user id)
        val namePartsUser = userEmail.split("@")[0].replace(".", "")
        val fullNameUser = namePartsUser.trim()
        val namePartsTarget = targetEmail.split("@")[0].replace(".", "")
        val fullNameTarget = namePartsTarget.trim()

        // connecting the user
        val user = User(id = fullNameUser)
        val token = client.devToken(user.id)
        client.connectUser(user, token).enqueue { result ->
            if (result.isSuccess) {
                // creating channel name
                val extraData = mutableMapOf<String, Any>()
                extraData["name"] = fullNameUser + " and " + fullNameTarget
                // creating channel
                client.createChannel(
                    channelType = "messaging",
                    channelId = "",
                    memberIds = listOf(fullNameUser, fullNameTarget),
                    extraData = extraData,
                ).enqueue { result ->
                    if (result.isSuccess) {
                        val channel = result.data()
                        val intent = MessagesActivity.getIntent(context, channelId = channel.cid)
                        ContextCompat.startActivity(context, intent, null)
                    } else {
                        println("create channel failed")
                    }
                }
            } else {
                println("connection not successful")
            }
        }
    }
}
