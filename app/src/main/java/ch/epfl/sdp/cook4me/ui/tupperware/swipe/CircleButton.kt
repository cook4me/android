package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    color: Color
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .size(56.dp)
            .border(2.dp, color, CircleShape),
        onClick = onClick,
    ) {
        Icon(
            icon, null,
            tint = Color.White
        )
    }
}
