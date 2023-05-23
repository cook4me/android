package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.RecipeFeedService
import ch.epfl.sdp.cook4me.persistence.model.RecipeNote
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import ch.epfl.sdp.cook4me.ui.common.PlaceholderScreen
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import kotlinx.coroutines.launch

const val RECIPE_LIST_RATIO = 0.9F
const val EMPTY_SPACE_RATIO = 0.05F
const val MIDDLE_SPACE_RATIO = 0.5F

/**
 * Displays the recipe feed screen
 * @param service the service to use to get the recipes/notes and update the notes
 * @param onCreateNewRecipe the callback to call when the user wants to create a new recipe
 */
@Preview(showBackground = true)
@Composable
fun RecipeFeed(
    service: RecipeFeedService = RecipeFeedService(),
    onCreateNewRecipe: () -> Unit = {},
    isOnline: Boolean = true
) {
    val isOrderedByTopRecipes = remember {
        mutableStateOf(true)
    }
    val recipeList = remember {
        mutableStateOf(listOf<RecipeNote>())
    }

    val userVotes = remember {
        mutableStateOf(mapOf<String, Int>())
    }

    val isLoading = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        userVotes.value = service.getRecipePersonalVotes()
        recipeList.value = service.getRecipesWithNotes()
        isLoading.value = false
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .testTag(stringResource(R.string.recipe_feed_screen_tag)),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        CreateNewItemButton(itemType = "Recipe", onClick = onCreateNewRecipe, canClick = isOnline)
        if (isLoading.value) {
            LoadingScreen()
        } else if (recipeList.value.isEmpty()) {
            PlaceholderScreen(image = R.drawable.recipe_book, text = R.string.empty_recipe_feed)
        } else {
            Box(modifier = Modifier.fillMaxHeight(RECIPE_LIST_RATIO)) {
                RecipeListScreen(
                    recipeList = if (isOrderedByTopRecipes.value) {
                        recipeList.value.sortedByDescending { it.note }
                    } else {
                        recipeList.value.sortedByDescending { it.recipe.creationTime }
                    },
                    onNoteUpdate = { recipe, note ->
                        // launch coroutine to update the note
                        coroutineScope.launch {
                            service.updateRecipeNotes(recipe, note)
                        }
                    },
                    userVotes = userVotes.value,
                    isOnline = isOnline
                )
            }
            Box(modifier = Modifier.fillMaxHeight(EMPTY_SPACE_RATIO))
            BottomBar(
                onButtonClicked = {
                    isOrderedByTopRecipes.value = it
                }
            )
        }
    }
}
