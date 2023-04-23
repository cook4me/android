package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.persistence.model.RecipeNote
import ch.epfl.sdp.cook4me.ui.recipe.feed.RecipeDisplay

/**
 * Displays a scrollable list of recipes
 * @param recipeList the list of (id with the recipe) with the note to display
 * @param onNoteUpdate the function to call when the note is updated (recipeId, note)
 */
@Composable
fun RecipeListScreen(recipeList: List<RecipeNote>, onNoteUpdate: (String, Int) -> Unit) {
    LazyColumn {
        items(recipeList.size) { index ->
            RecipeDisplay(
                recipeList[index].recipe, recipeList[index].note,
                onNoteUpdate = { note -> onNoteUpdate(recipeList[index].recipeId, note) }
            )
        }
    }
}
