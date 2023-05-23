package ch.epfl.sdp.cook4me.ui.common

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ch.epfl.sdp.cook4me.R
import kotlinx.coroutines.delay

const val DELAY_TIME = 100L
const val MOD_NUMBER = 3
const val ADD_NUMBER = 1

// A loading screen that is displayed when the user is waiting for the chat to load
// It shows a changing text with different number of dots
@Composable
fun LoadingScreen() {
    val loadingDots = remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY_TIME)
            loadingDots.value = loadingDots.value % MOD_NUMBER + ADD_NUMBER
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.Loading_Screen_Tag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "${stringResource(R.string.loading_text)}${".".repeat(loadingDots.value)}"
        )
    }
}
