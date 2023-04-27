package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes

// not used now.
@Composable
fun MessageScreen(
    channelId: String,
    onBackListener: () -> Unit = {},
) {
    ChatTheme(
        shapes = StreamShapes.defaultShapes().copy(
            avatar = RoundedCornerShape(8.dp),
            attachment = RoundedCornerShape(16.dp),
            myMessageBubble = RoundedCornerShape(16.dp),
            otherMessageBubble = RoundedCornerShape(16.dp),
            inputField = RectangleShape
        )
    ) {
        MessagesScreen(
            channelId = channelId,
            messageLimit = 30,
            onBackPressed = { onBackListener() },
        )
    }
}
