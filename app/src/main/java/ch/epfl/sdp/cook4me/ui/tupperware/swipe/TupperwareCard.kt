package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.swipableCard

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun TupperwareCard(
    state: SwipeableCardState,
    tupperware: TupperwareWithImage,
    onSwiped: (direction: Direction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .swipableCard(
                state = state,
                blockedDirections = listOf(Direction.Up, Direction.Down),
                onSwiped = onSwiped
            )
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tupperware.image)
                    .build(),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = tupperware.title,
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = tupperware.description,
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}
