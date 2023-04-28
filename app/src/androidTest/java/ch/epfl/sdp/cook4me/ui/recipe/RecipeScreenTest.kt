package ch.epfl.sdp.cook4me.ui.recipe

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val dummyRecipe = Recipe(
        name = "Pizza", ingredients = listOf("Tomato", "Cheese", "Dough"),
        recipeSteps = listOf("Put the tomato on the dough", "Put the cheese on the tomato"),
        difficulty = "Easy", cookingTime = "30min", servings = 4
    )

    @Test
    fun titleCorrectlyDisplayed() {
        composeTestRule.setContent {
            RecipeScreen(dummyRecipe)
        }
        composeTestRule.onNodeWithText(dummyRecipe.name).assertExists()
    }

    @Test
    fun basicRecipeInfoDisplayedWithSeparator() {
        composeTestRule.setContent {
            RecipeScreen(dummyRecipe)
        }
        val recipeBasicInfo = "Servings: ${dummyRecipe.servings} | Cooking time: ${dummyRecipe.cookingTime} | Difficulty: ${dummyRecipe.difficulty}"
        composeTestRule.onNodeWithText(recipeBasicInfo).assertExists()
    }

    @Test
    fun ingredientsAndStepsDisplayedAsDashedList() {
        composeTestRule.setContent {
            RecipeScreen(dummyRecipe)
        }
        val ingredients = "Ingredients: \n ${dummyRecipe.ingredients.map{s -> "\t - $s"}.joinToString("\n")}"
        val steps = "Steps: \n ${dummyRecipe.recipeSteps.map{s -> "\t - $s"}.joinToString("\n")}"
        composeTestRule.onNodeWithText(ingredients).assertExists()
        composeTestRule.onNodeWithText(steps).assertExists()
    }
}
