package ch.epfl.sdp.cook4me.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import kotlinx.coroutines.delay

val visibility = listOf<Float>(0.4f, 0.6f, 0.8f, 1.0f)
const val DELAY_TIME = 80L
const val MOD_NUMBER = 4

@Composable
fun LoadingScreen() {
    val imageResource = R.mipmap.ic_launcher_foreground
    val loadingIndex = remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY_TIME)
            loadingIndex.value = (loadingIndex.value + 1) % MOD_NUMBER
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.Loading_Screen_Tag)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = "Loading icon",
                alpha = visibility[loadingIndex.value],
                modifier = Modifier
                    .size(200.dp) // Change the size as needed
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = stringResource(id = R.string.Loading_Screen_Text),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
        }
    }
}
