package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import com.alexstyl.swipeablecard.SwipeableCardState

data class Tupp(
    val title: String,
    val description: String,
    val imageId: Int
)

val tupperwareList = listOf(
    Tupp(
        title = "Spaghetti Carbonara",
        description = "made by my mother with much love ❤️",
        imageId = R.drawable.placeholder_carbonara
    ),
    Tupp(
        title = "Guacamole",
        description = "super guaca️",
        imageId = R.drawable.placeholder_guacamole
    ),
    Tupp(
        title = "Tupperware",
        description = "Look at this tup",
        imageId = R.drawable.placeholder_tupperware
    )
)

@Composable
fun TupperwareSwipeScreen(
    onCreateNewTupperware: () -> Unit = {},
) {
    val states = tupperwareList.map { it to rememberSwipeableCardState() }
    val scope = rememberCoroutineScope()
    val allDone = states.all { it.second.swipedDirection != null }

    Column {
        CreateNewItemButton(itemType = "Tupperware", onClick = onCreateNewTupperware)
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                Modifier
                    .padding(24.dp)
                    .weight(0.2f),
//                .fillMaxSize()
//                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                if (allDone) {
                    Text("all done")
                } else {
                    states.forEach { (tupperware, state) ->
                        if (state.swipedDirection == null) {
                            TupperwareCard(
                                state,
                                tupperware = tupperware
                            )
                        }
                    }
                }
            }

            if (!allDone) {
                Row(
                    Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircleButton(
                        onClick = {
                            scope.launch {
                                val last = states
                                    .reversed()
                                    .firstOrNull {
                                        it.second.offset.value == Offset(0f, 0f)
                                    }?.second
                                last?.swipe(Direction.Left)
                            }
                        },
                        icon = Icons.Rounded.Close
                    )
                    CircleButton(
                        onClick = {
                                  //TODO
                        },
                        icon = Icons.Rounded.Info
                    )
                    CircleButton(
                        onClick = {
                            scope.launch {
                                val last = states
                                    .reversed()
                                    .firstOrNull {
                                        it.second.offset.value == Offset(0f, 0f)
                                    }?.second
                                last?.swipe(Direction.Right)
                            }
                        },
                        icon = Icons.Rounded.Favorite
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
private fun TupperwareCard(
    state: SwipeableCardState,
    tupperware: Tupp,
) {
    Card(modifier = Modifier
        .fillMaxSize()
        .swipableCard(
            state = state,
            blockedDirections = listOf(Direction.Up, Direction.Down),
            onSwiped = {
                //already handled by the callbacks of the buttons
            }
        )) {
        if (tupperware.title == "Guacamole") {
         Box {
             Text(tupperware.description)
         }
        }
        else {
                Box {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(tupperware.imageId),
                        contentDescription = null,
                    )
                    Column(Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = tupperware.title,
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
        }
    }
}

@Composable
private fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .size(56.dp)
            .border(2.dp, MaterialTheme.colors.primary, CircleShape),
        onClick = onClick
    ) {
        Icon(
            icon, null,
            tint = MaterialTheme.colors.onPrimary
        )
    }
}
