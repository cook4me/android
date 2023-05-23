package ch.epfl.sdp.cook4me.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R

@Composable
fun PlaceholderScreen(
    @DrawableRes image: Int,
    @StringRes text: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = "${stringResource(text)} " +
                stringResource(R.string.placeholder_screen_content_description),
            alpha = 0.55f,
            modifier = Modifier.fillMaxSize(fraction = 0.55f)
        )
        Text(
            text = stringResource(text),
            fontSize = 16.sp,
            modifier = Modifier.alpha(alpha = 0.55f)
        )
    }
}
