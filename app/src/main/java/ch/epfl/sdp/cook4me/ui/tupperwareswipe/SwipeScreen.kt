package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import kotlinx.coroutines.launch

private data class TupperwareState(
    val id: String,
    val data: TupperwareWithImage,
    val cardState: SwipeableCardState
)

// code inspired by https://github.com/alexstyl/compose-tinder-card/blob/main/app/src/main/java/com/alexstyl/swipeablecard/MainActivity.kt
@Composable
fun TupperwareSwipeScreen(
    onCreateNewTupperware: () -> Unit = {},
    swipeService: SwipeService = SwipeService()
) {
    val data = remember {
        mutableStateOf(mapOf<String, TupperwareWithImage>())
    }
    val states =
        data.value.map { TupperwareState(it.key, it.value, rememberSwipeableCardState()) }
    val scope = rememberCoroutineScope()
    val allDone =
        states.isEmpty() || states.all { it.cardState.swipedDirection != null }
    val openMatchDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        data.value = swipeService.getAllUnswipedTupperware()
        isLoading.value = false
    }

    if (openMatchDialog.value) {
        MatchDialog {
            openMatchDialog.value = false
        }
    }

    suspend fun onSwipe(tupperwareId: String, direction: Direction) {
        swipeService.storeSwipeResult(tupperwareId, direction == Direction.Right)
        if (direction == Direction.Right && swipeService.isMatch(tupperwareId)) {
            openMatchDialog.value = true
        }
    }

    fun notSwipedYet(offset: Offset) = offset == Offset(
        x = 0f,
        y = 0f
    )

    fun findElementToBeSwipedOrNull() =
        states
            .reversed()
            .firstOrNull { notSwipedYet(it.cardState.offset.value) }

    fun onSwipeButtonClicked(direction: Direction) {
        scope.launch {
            val element = findElementToBeSwipedOrNull()
            element?.let {
                // for the buttons we need to manually set the offset, because no dragging was done
                it.cardState.swipe(direction)
                onSwipe(it.id, direction)
            }
        }
    }

    Column {
        CreateNewItemButton(itemType = "Tupperware", onClick = onCreateNewTupperware)
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isLoading.value) {
                LoadingScreen()
            } else {
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
                        states.forEach {
                            if (it.cardState.swipedDirection == null) {
                                TupperwareCard(
                                    it.cardState,
                                    it.data
                                ) { direction ->
                                    scope.launch {
                                        onSwipe(it.id, direction)
                                    }
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
                        onSwipeButtonClicked(Direction.Left)
                    },
                    enabled = !allDone,
                    icon = Icons.Rounded.Close,

                )
                CircleButton(
                    onClick = {
                        onSwipeButtonClicked(Direction.Right)
                    },
                    enabled = !allDone,
                    icon = Icons.Rounded.Favorite
                )
            }
        }
    }
}
