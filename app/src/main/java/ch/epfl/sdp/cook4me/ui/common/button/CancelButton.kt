package ch.epfl.sdp.cook4me.ui.common.button

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CancelButton(
    text: String,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
) {
    OutlinedButton(onCancelClick, modifier) {
        Text(text = text, fontSize = 16.sp)
    }
}
