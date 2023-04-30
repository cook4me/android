package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.persistence.model.Recipe

const val RECIPE_TITLE_RATIO = 0.8F

/**
 * Displays a single recipe
 * @param recipe the recipe to display
 * @param note the note of the recipe
 * @param onNoteUpdate the function to call when the note is updated
 * @param userVote the vote of the user
 */
@Composable
fun RecipeDisplay(recipe: Recipe, note: Int, onNoteUpdate: (Int) -> Unit = {}, userVote: Int = 0) {
    val clicked = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { clicked.value = !clicked.value }
            .background(Color.Gray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(RECIPE_TITLE_RATIO)
                    .padding(8.dp),
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
                Text(
                    text = recipe.cookingTime,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
                if (clicked.value) {
                    Text(
                        text = "This is a recipe",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )
                }
            }
            // put on the right side a voteIcon
            VoteIcon(counterValue = note, onChange = onNoteUpdate, userVote = userVote)
        }
    }
}
