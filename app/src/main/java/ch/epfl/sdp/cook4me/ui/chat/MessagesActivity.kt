package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes

// The activity to show the message screen. Couldn't have done with just
// one activity (see: https://github.com/cook4me/android/issues/155)
class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)

        if (channelId == null) {
            finish()
            return
        }

        setContent {
            Cook4meTheme {
                ChatTheme(
                    shapes = StreamShapes.defaultShapes().copy(
                        avatar = RoundedCornerShape(8.dp),
                        attachment = RoundedCornerShape(16.dp),
                        myMessageBubble = RoundedCornerShape(16.dp),
                        otherMessageBubble = RoundedCornerShape(16.dp),
                        inputField = RectangleShape
                    ),
                    colors = StreamColors.defaultColors().copy(
                        errorAccent = MaterialTheme.colors.onError,
                        primaryAccent = MaterialTheme.colors.primary,
                    )
                ) {
                    MessagesScreen(
                        channelId = channelId,
                        messageLimit = 30,
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent =
            Intent(context, MessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
    }
}
