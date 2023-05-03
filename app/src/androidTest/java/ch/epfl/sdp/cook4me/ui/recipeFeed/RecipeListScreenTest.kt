package ch.epfl.sdp.cook4me.ui.recipeFeed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.model.RecipeNote
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultRecipeListScreenIsDisplayed() {
        val mockList = listOf(
            RecipeNote(recipeId = "id1", recipe = Recipe(name = "Test recipe 1", cookingTime = "10 min"), note = 1),
            RecipeNote(recipeId = "id2", recipe = Recipe(name = "Test recipe 2", cookingTime = "20 min"), note = 2)
        )
        composeTestRule.setContent {
            RecipeListScreen(recipeList = mockList, onNoteUpdate = { _, _ -> }, userVotes = emptyMap())
        }

        composeTestRule.onNodeWithText("Test recipe 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Test recipe 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("20 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun clickingOnRecipeDisplaysEntireRecipe() {
        val mockRecipe = Recipe(
            name = "Test recipe",
            cookingTime = "10 min",
            ingredients = listOf("ingredient1", "ingredient2"),
        )
        val ingredients = "Ingredients: \n ${mockRecipe.ingredients.map{s -> "\t - $s"}.joinToString("\n")}"
        val mockList = listOf(
            RecipeNote(recipeId = "id1", recipe = mockRecipe, note = 1),
            RecipeNote(recipeId = "id2", recipe = Recipe(name = "Test recipe 2", cookingTime = "20 min"), note = 2)
        )
        composeTestRule.setContent {
            RecipeListScreen(recipeList = mockList, onNoteUpdate = { _, _ -> })
        }

        composeTestRule.onNodeWithText(ingredients).assertDoesNotExist()
        composeTestRule.onNodeWithText("Test recipe").performClick()
        composeTestRule.onNodeWithText(ingredients).assertIsDisplayed()
    }
}
