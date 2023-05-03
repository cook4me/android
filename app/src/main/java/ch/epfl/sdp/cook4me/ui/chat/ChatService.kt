package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import androidx.core.content.ContextCompat
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User

/**
 * create a chat with the given email and go to the corresponding message screen
 *
 * @param targetEmail: the email of the user who is the current user is going to
 * start a chat with.
 * @param accountService: the account service, by default, refer to application/AccountService for a look.
 * @param client: the chat client, refer to overviewScreen or ChannelScreen for a look.
 * @param context: the context of the current activity, refer to overviewScreen or ChannelScreen for a look.
 *                  usually is Localcontext.current
 */
fun createChatWithEmail(
    targetEmail: String,
    accountService: AccountService = AccountService(),
    client: ChatClient,
    context: Context,
) {
    val userEmail = accountService.getCurrentUserWithEmail()
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
                // only proceed if connection is successful
                // creating channel name, it will be showed at the top of the message screen
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
                        // if channel successfully created, go to the message screen
                        // when there already exists a channel between the two users,
                        // the result is still successful, and therefore jumping
                        // to the message screen
                        val channel = result.data()
                        // starting the message activity
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

/**
 * create a chat with the given email pairs
 *
 * @param userEmail: the email of one user
 * @param targetEmail: the email of the other user
 * @param client: the chat client, refer to overviewScreen or ChannelScreen for a look.
 *
 * I'm not defining any actions upon a successful creation of a channel, will do later when
 * we have a clear logic of tups matches.
 */
fun createChatWithPairs(
    userEmail: String,
    targetEmail: String,
    client: ChatClient,
) {
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
            // only proceed if connection is successful
            // creating channel name, it will be showed at the top of the message screen
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
                    // if channel successfully created, go to the message screen
                    // when there already exists a channel between the two users,
                    // the result is still successful, and therefore jumping
                    // to the message screen
                    val channel = result.data()
                    // do nothing now
                        /*val intent = MessagesActivity.getIntent(context, channelId = channel.cid)
                        ContextCompat.startActivity(context, intent, null)*/
                } else {
                    println("create channel failed")
                }
            }
        } else {
            println("connection not successful")
        }
    }
}
