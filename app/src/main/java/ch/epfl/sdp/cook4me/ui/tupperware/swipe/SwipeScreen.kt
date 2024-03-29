package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.SwipeService
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import ch.epfl.sdp.cook4me.ui.common.PlaceholderScreen
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import ch.epfl.sdp.cook4me.ui.theme.errorRed
import ch.epfl.sdp.cook4me.ui.theme.supportingYellow
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
@Suppress("ComplexMethod")
@Composable
fun TupperwareSwipeScreen(
    onCreateNewTupperware: () -> Unit = {},
    swipeService: SwipeService = SwipeService(),
    accountService: AccountService = AccountService(),
    isOnline: Boolean = true,
) {
    var data by remember {
        mutableStateOf(mapOf<String, TupperwareWithImage>())
    }
    val states =
        data.map { TupperwareState(it.key, it.value, rememberSwipeableCardState()) }
    val scope = rememberCoroutineScope()
    val allDone =
        states.isEmpty() || states.all { it.cardState.swipedDirection != null }
    var openMatchDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var otherUser by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (isOnline) {
        LaunchedEffect(Unit) {
            data = swipeService.getAllUnswipedTupperware()
            isLoading = false
        }
    } else {
        isLoading = false
    }

    if (openMatchDialog) {
        accountService.getCurrentUserWithEmail()?.let {
            MatchDialog(
                user = it,
                otherUser = otherUser,
                context = context, // chat service requires context to start a chat
                onDismissRequest = { openMatchDialog = false }
            )
        }
    }

    suspend fun onSwipe(tupperwareId: String, direction: Direction) {
        swipeService.storeSwipeResult(tupperwareId, direction == Direction.Right)
        if (direction == Direction.Right && swipeService.isMatch(tupperwareId)) {
            swipeService.getUserByTupperwareId(tupperwareId)?.let {
                otherUser = it
                openMatchDialog = true
            }
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
        CreateNewItemButton(
            itemType = "Tupperware",
            modifier = Modifier.testTag(stringResource(R.string.tupperware_swipe_screen_tag)),
            onClick = onCreateNewTupperware,
            canClick = isOnline
        )
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isLoading) {
                LoadingScreen()
            } else if (allDone) {
                PlaceholderScreen(
                    R.drawable.food_container,
                    if (isOnline) R.string.swipe_alldone_placeholder else R.string.to_swipe_go_online
                )
            } else {
                Box(
                    Modifier
                        .padding(16.dp)
                        .weight(weight = 0.87f),
                    contentAlignment = Alignment.Center
                ) {
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
                        icon = Icons.Rounded.Close,
                        color = errorRed
                    )
                    CircleButton(
                        onClick = {
                            onSwipeButtonClicked(Direction.Right)
                        },
                        icon = Icons.Rounded.Favorite,
                        color = supportingYellow
                    )
                }
            }
        }
    }
}
