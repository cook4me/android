package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import android.util.Log
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
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import kotlinx.coroutines.launch

// code heavily inspired by https://github.com/alexstyl/compose-tinder-card/blob/main/app/src/main/java/com/alexstyl/swipeablecard/MainActivity.kt
@Composable
fun TupperwareSwipeScreen(
    onCreateNewTupperware: () -> Unit = {},
    swipeService: SwipeService = SwipeService()
) {

    val tupperwareList = remember {
        mutableStateOf(listOf<Pair<String, TupperwareWithImage?>>())
    }
    val states = tupperwareList.value.map { it.first to (it.second to rememberSwipeableCardState()) }
    val scope = rememberCoroutineScope()
    val allDone = states.isEmpty() || states.all { it.second.second.swipedDirection != null }
    val openMatchDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        tupperwareList.value = swipeService.getAllUnswipedTupperware().toList()
        Log.e("SwipeScreen", "done heeere")
        //TODO: show loading screen
        isLoading.value = false
    }

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
                    it.second.second.offset.value == Offset(
                        x = 0f,
                        y = 0f
                    ) // otherwise the circle buttons don't work
                }?.second
            last?.second?.swipe(direction)
//            swipeRepository.add()
//            if(swipeService.isMatch()) {
//                openMatchDialog.value = true
//            }
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
                        states.forEach { (tupperware, somePair) ->
                            somePair.first?.let { data ->
                                if (somePair.second.swipedDirection == null) {
                                    TupperwareCard(
                                        somePair.second,
                                        tupperware = data,
                                    ) {
                                        scope.launch {
                                            openMatchDialog.value = true
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
}