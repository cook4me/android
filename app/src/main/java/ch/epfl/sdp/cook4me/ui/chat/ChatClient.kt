package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
fun provideChatClient(apiKey: String, context: Context) =
    ChatClient.Builder(apiKey, context)
        .withPlugin(provideOfflinePluginFactory(context))
        .logLevel(ChatLogLevel.ALL)
        .build()

private fun provideOfflinePluginFactory(appContext: Context): StreamOfflinePluginFactory =
    StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
        ),
        appContext = appContext
    )
