package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes

// The activity to show the message screen. Couldn't have done with just
// one activity (see: https://github.com/cook4me/android/issues/155)
class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        val userEmail = intent.getStringExtra(KEY_USER_EMAIL)

        if (channelId == null) {
            finish()
            return
        }

        setContent {
            ChatTheme(
                shapes = StreamShapes.defaultShapes().copy(
                    avatar = RoundedCornerShape(8.dp),
                    attachment = RoundedCornerShape(16.dp),
                    myMessageBubble = RoundedCornerShape(16.dp),
                    otherMessageBubble = RoundedCornerShape(16.dp),
                    inputField = RectangleShape
                ),
                imageLoaderFactory = CoilImageLoaderFactory
            ) {
                MessagesScreen(
                    channelId = channelId,
                    messageLimit = 30,
                    onBackPressed = { finish() },
                    onHeaderActionClick = { channel ->
                        val targetMember = channel.members.find { it.user.extraData["email"] != userEmail }
                        val targetEmail = targetMember?.user?.extraData?.get("email")
                        if (targetEmail != null) {
                            val intent = ChatProfileActivity.getIntent(this, targetEmail as String)
                            startActivity(intent)
                        }
                    }
                )
            }
        }
    }
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"
        private const val KEY_USER_EMAIL = "userEmail"

        fun getIntent(context: Context, channelId: String, userEmail: String?): Intent =
            Intent(context, MessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
                putExtra(KEY_USER_EMAIL, userEmail)
            }
    }
}
