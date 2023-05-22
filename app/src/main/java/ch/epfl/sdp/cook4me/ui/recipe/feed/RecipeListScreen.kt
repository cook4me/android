package ch.epfl.sdp.cook4me.ui.recipe.feed

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.RecipeNote

/**
 * Displays a scrollable list of recipes
 * @param recipeList the list of (id with the recipe) with the note to display
 * @param onNoteUpdate the function to call when the note is updated (recipeId, note)
 * @param userVotes the map of the user votes (recipeId, note) if no votes -> empty key
 */
@Composable
fun RecipeListScreen(
    recipeList: List<RecipeNote>,
    onNoteUpdate: (String, Int) -> Unit,
    userVotes: Map<String, Int>,
    isOnline: Boolean = true
) {
    val expandedRecipe = remember { mutableStateOf<Int?>(null)}
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(recipeList.size) { index ->
            RecipeDisplay(
                recipe = recipeList[index].recipe,
                note = recipeList[index].note,
                image = Uri.parse("android.resource://ch.epfl.sdp.cook4me/" + R.drawable.tacos_placefolder),
                onNoteUpdate = { note -> onNoteUpdate(recipeList[index].recipeId, note) },
                userVote = userVotes[recipeList[index].recipeId] ?: 0,
                canClick = isOnline,
                onClick = { expandedRecipe.value = if (expandedRecipe.value == index) null else index },
                isExpanded = expandedRecipe.value == index
            )
        }
    }
}
