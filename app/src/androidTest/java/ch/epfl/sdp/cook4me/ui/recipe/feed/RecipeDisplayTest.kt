package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDisplayTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultRecipeDisplayIsDisplayed() {
        val recipe = Recipe(name = "Test recipe", cookingTime = "10 min")
        composeTestRule.setContent {
            RecipeDisplay(recipe = recipe, note = 1)
        }

        composeTestRule.onNodeWithText("Test recipe").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }
}
