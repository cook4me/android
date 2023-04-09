package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import ch.epfl.sdp.cook4me.persistence.model.Recipe

/**
 * Displays a scrollable list of recipes
 * @param recipeList the list of recipes to display with the note
 */
@Composable
fun RecipeListScreen(recipeList: List<Pair<Recipe,Int>>) {
    LazyColumn {
        items(recipeList.size) { index ->
            RecipeDisplay(recipeList[index].first, recipeList[index].second)
        }
    }
}