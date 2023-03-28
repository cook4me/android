package ch.epfl.sdp.cook4me.ui.common.button
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingButton(text: String, modifier: Modifier = Modifier, isLoading: Boolean = false, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        enabled = !isLoading
    ) {
        Text(text = text, fontSize = 16.sp)
        AnimatedVisibility(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(start = 16.dp).size(16.dp),
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
            )
        }
    }
}
