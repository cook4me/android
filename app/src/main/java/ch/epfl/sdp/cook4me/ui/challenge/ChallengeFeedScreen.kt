package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton

@Composable
fun ChallengeFeedScreen(
    onCreateNewChallengeClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CreateNewItemButton(itemType = "Challenge", onClick = onCreateNewChallengeClick)
    }
}
