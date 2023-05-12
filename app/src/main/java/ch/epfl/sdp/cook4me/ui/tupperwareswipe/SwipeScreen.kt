package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.SwipeService
import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import kotlinx.coroutines.launch

// code heavily inspired by https://github.com/alexstyl/compose-tinder-card/blob/main/app/src/main/java/com/alexstyl/swipeablecard/MainActivity.kt
@Composable
fun TupperwareSwipeScreen(
    onCreateNewTupperware: () -> Unit = {},
) {
    val states = tupperwareList.map { it to rememberSwipeableCardState() }
    val scope = rememberCoroutineScope()
    val allDone = states.isEmpty() || states.all { it.second.swipedDirection != null }

    val openMatchDialog = remember { mutableStateOf(false) }

    if (openMatchDialog.value) {
        MatchDialog {
            openMatchDialog.value = false
        }
    }

    fun swipe(direction: Direction) {
        scope.launch {
            val last = states
                .reversed()
                .firstOrNull {
                    it.second.offset.value == Offset(
                        x = 0f,
                        y = 0f
                    ) // otherwise the circle buttons don't work
                }?.second
            last?.swipe(direction)
        }
    }

    Column {
        CreateNewItemButton(itemType = "Tupperware", onClick = onCreateNewTupperware)
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                Modifier
                    .padding(16.dp)
                    .weight(weight = 0.87f)
                    .testTag(stringResource(R.string.tupperware_swipe_screen_tag)),
                contentAlignment = Alignment.Center
            ) {
                if (allDone) {
                    Text("all done") // TODO: https://github.com/cook4me/android/issues/185
                } else {
                    states.forEach { (tupperware, state) ->
                        if (state.swipedDirection == null) {
                            TupperwareCard(
                                state,
                                tupperware = tupperware,
                            ) {
                                scope.launch {
                                    openMatchDialog.value = true
                                }
                            }
                        }
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .weight(weight = 0.13f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircleButton(
                    onClick = {
                        swipe(Direction.Left)
                    },
                    enabled = !allDone,
                    icon = Icons.Rounded.Close,

                    )
                // TODO: https://github.com/cook4me/android/issues/189
//                CircleButton(
//                    onClick = {
//                    },
//                    enabled = !allDone,
//                    icon = Icons.Rounded.Info
//                )
                CircleButton(
                    onClick = {
                        swipe(Direction.Right)
                    },
                    enabled = !allDone,
                    icon = Icons.Rounded.Favorite
                )
            }
        }
    }
}

// TODO: will be removed with https://github.com/cook4me/android/issues/183
data class DummyTupperware(
    val title: String,
    val description: String,
    val imageId: Int
)

val tupperwareList = listOf(
    DummyTupperware(
        title = "Spaghetti Carbonara",
        description = "made by my mother with much love ❤️",
        imageId = R.drawable.placeholder_carbonara
    ),
    DummyTupperware(
        title = "Guacamole",
        description = "Video provides a powerful way to help you prove your point. When you click Online Video, you can paste in the embed code for the video you want to add. You can also type a keyword to search online for the video that best fits your document.\n" +
                "To make your document look professionally produced, Word provides header, footer, cover page and text box designs that complement each other. For example, you can add a matching cover page, header and sidebar.",
        imageId = R.drawable.placeholder_guacamole
    ),
    DummyTupperware(
        title = "Tupperware",
        description = "Look at this tup",
        imageId = R.drawable.placeholder_tupperware
    )
)
