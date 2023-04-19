package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.persistence.model.Recipe

/**
 * Displays a scrollable list of recipes
 * @param recipeList the list of (id with the recipe) with the note to display
 * @param onNoteUpdate the function to call when the note is updated (recipeId, note)
 */
@Composable
fun RecipeListScreen(recipeList: List<Pair<Pair<String, Recipe>, Int>>, onNoteUpdate: (String, Int) -> Unit) {
    LazyColumn {
        items(recipeList.size) { index ->
            RecipeDisplay(
                recipeList[index].first.second, recipeList[index].second,
                onNoteUpdate = { note -> onNoteUpdate(recipeList[index].first.first, note) }
            )
        }
    }
}
