package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ch.epfl.sdp.cook4me.persistence.model.Recipe

val mockList = listOf(Recipe(name = "Recipe 1"), Recipe(name = "Recipe 2"), Recipe(name = "Recipe 3"),
     Recipe(name = "Recipe 4"), Recipe(name = "Recipe 5"), Recipe(name = "Recipe 6"))

/**
 * Displays a scrollable list of recipes
 * @param recipeList the list of recipes to display
 */
@Preview(showBackground = true)
@Composable
fun RecipeListScreen(recipeList: List<Recipe> = mockList) {
    LazyColumn {
        items(recipeList.size) { index ->
            RecipeDisplay(recipeList[index])
        }
    }
}