package ch.epfl.sdp.cook4me.ui.recipe.feed

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.persistence.model.Recipe
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
    isExpanded: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier,
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            Modifier.clickable(enabled = canClick, onClick = onClick)
        ) {
            Column (modifier = Modifier.aspectRatio(1.1f)) {
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = recipe.name,
                            color = Color.White,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                        )
                        VoteIcon(
                            modifier = Modifier,
                            counterValue = note,
                            onChange = onNoteUpdate,
                            userVote = userVote
                        )
                    }
                }
                // Display cooking time, difficulty and servings count at the bottom
                BasicInformationLabel(
                    modifier = Modifier.weight(0.16f),
                    cookingTime = recipe.cookingTime,
                    difficulty = recipe.difficulty,
                    servingsCount = recipe.servings,
                    likes = note,
                    onNoteUpdate = onNoteUpdate
                )
            }
            if (isExpanded) {
                IngredientsAndInstructions(
                    ingredients = recipe.ingredients,
                    instructions = recipe.recipeSteps
                )
            }
        }
    }

}

@Composable
fun BasicInformationLabel(
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
    }
}

@Composable
fun IngredientsAndInstructions(
    ingredients: List<String>,
    instructions: List<String>
) {
    Divider(thickness = 0.45.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp),
        horizontalAlignment = Alignment.Start,

        ) {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.size(10.dp))
        ingredients.forEach { ingredient ->
            Text(
                text = "• $ingredient",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.size(15.dp))
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.size(10.dp))
        instructions.forEachIndexed { index, instruction ->
            val stepText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                ) {
                    append("${index + 1}.")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                ) {
                    append(" $instruction")
                }
            }
            Text(
                text = stepText,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp, top = 3.dp, bottom = 3.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}
