package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient

// create a chat with the given email and go to channel screen
fun createChatWithEmail(
    targetEmail: String,
    accountService: AccountService = AccountService(),
    client: ChatClient,
) {
    val userEmail = accountService.getCurrentUserEmail()
    userEmail?.let{
        // parsing email to get the name (user id)
        val namePartsUser = userEmail.split("@")[0].replace(".", "")
        val fullNameUser = namePartsUser.trim()
        val namePartsTarget = targetEmail.split("@")[0].replace(".", "")
        val fullNameTarget = namePartsTarget.trim()
        // creating channel name
        val extraData = mutableMapOf<String, Any>()
        extraData["name"] = fullNameUser + "and" + fullNameTarget
        // creating channel
        client.createChannel(
            channelType = "messaging",
            channelId = "",
            memberIds = listOf(fullNameUser, fullNameTarget),
            extraData = extraData,
        ).enqueue { result ->
            if (result.isSuccess) {
                val channel = result.data()
                println("created!!")
            } else {
                println("create channel failed")
            }
        }
    }
}