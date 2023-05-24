package ch.epfl.sdp.cook4me.ui.map.buttons

import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import ch.epfl.sdp.cook4me.R

@Composable
fun ButtonUNIL(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_unil),
            contentDescription = "UNIL Logo"
        )
    }
}
