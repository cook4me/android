package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val defaultTagColor = Color.Gray
private val defaultTextColor = Color.White

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = defaultTagColor,
    textColor: Color = defaultTextColor,
    elevation: Dp = 0.dp
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color,
        elevation = elevation
    ) {
        Text(text = text, color = textColor, modifier = Modifier.padding(5.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TagPreview() {
    Tag(text = "Pizza")
}
