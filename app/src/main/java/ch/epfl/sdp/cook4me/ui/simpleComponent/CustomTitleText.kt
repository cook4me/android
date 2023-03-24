package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CustomTitleText(text: String = "") {
    Text(
        modifier = Modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}
