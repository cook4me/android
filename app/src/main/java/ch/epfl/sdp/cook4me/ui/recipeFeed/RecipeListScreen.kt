package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
    isOnline: Boolean
) {
    LazyColumn {
        items(recipeList.size) { index ->
            RecipeDisplay(
                recipe = recipeList[index].recipe,
                note = recipeList[index].note,
                onNoteUpdate = { note -> onNoteUpdate(recipeList[index].recipeId, note) },
                userVote = userVotes[recipeList[index].recipeId] ?: 0,
                canClick = isOnline
            )
        }
    }
}
