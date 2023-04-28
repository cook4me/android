package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import ch.epfl.sdp.cook4me.R
import kotlinx.coroutines.delay

@Composable
@Suppress("MagicNumber")
fun LoadingScreen() {
    val loadingDots = remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            loadingDots.value = loadingDots.value % 3 + 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LocalContext.current.getString(R.string.Loading_Screen_Tag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Now loading${".".repeat(loadingDots.value)}"
        )
    }
}
