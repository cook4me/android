package ch.epfl.sdp.cook4me.ui.recipeFeed

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.ui.recipe.RecipeScreen
import coil.compose.AsyncImage

const val RECIPE_TITLE_RATIO = 0.8F

/**
 * Displays a single recipe
 * @param recipe the recipe to display
 * @param note the note of the recipe
 * @param onNoteUpdate the function to call when the note is updated
 * @param userVote the vote of the user
 */

@Composable
fun RecipeDisplay(
    recipe: Recipe,
    note: Int,
    image: Uri,
    onNoteUpdate: (Int) -> Unit = {},
    userVote: Int = 0,
    canClick: Boolean = true,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .aspectRatio(1.1f),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            Modifier.clickable(enabled = canClick, onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.84f)
            ) {
                // Use AsyncImage to load the image
                AsyncImage(
                    model = image.toString(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                // Adding a gradient overlay to the bottom of the image for text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 0.6f
                            )
                        )
                )

                // Recipe title at the bottom of the image
                Text(
                    text = recipe.name,
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
            // Display cooking time, difficulty and servings count at the bottom
            BottomUnexpanded(
                modifier = Modifier.weight(0.16f),
                cookingTime = recipe.cookingTime,
                difficulty = recipe.difficulty,
                servingsCount = recipe.servings,
                likes = note,
                onNoteUpdate = onNoteUpdate
            )
        }
    }

}

@Composable
fun BottomUnexpanded(
    modifier: Modifier = Modifier,
    cookingTime: String,
    difficulty: String,
    servingsCount: Int,
    likes: Int,
    onNoteUpdate: (Int) -> Unit,
) {
    val resizeFactor = 1f
    val textStyle = MaterialTheme.typography.body1.copy(
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.body1.fontSize * resizeFactor
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cooking time
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.AccessTime, contentDescription = "Cooking Time", modifier = Modifier.size(24.dp * resizeFactor))
            Text(
                text = cookingTime,
                style = textStyle,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Text(
            text = difficulty,
            style = textStyle
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Group, contentDescription = "Servings", modifier = Modifier.size(24.dp * resizeFactor))
            Text(
                text = "$servingsCount",
                style = textStyle,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        VoteIcon(
            counterValue = likes,
            onChange = onNoteUpdate
        )
    }
}