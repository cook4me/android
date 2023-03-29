package ch.epfl.sdp.cook4me.ui.recipeform

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import okhttp3.internal.wait
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }

    private val expectedRecipe = Recipe(
        name = "Sad Pizza",
        ingredients = listOf("flour", "water", "salt"),
        recipeSteps = listOf("Look at sad ingredients", "sob in bowl to add salt", "mix together", "enjoy"),
        servings = 1,
        difficulty = "Hard",
        cookingTime = "4h00",
        photos = listOf()
    )

    private val submitForm = { recipe: Recipe ->
        assert(recipe == expectedRecipe)
    }

    @Test
    fun validRecipeFormIsCorrectlySubmitted() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc)).performTextInput("Sad Pizza")
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc)).performTextInput("1")
        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc)).performTextInput("flour\nwater\nsalt")
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc)).performClick()
        composeTestRule.waitUntilExists(hasText("Hard"))
        composeTestRule.onNodeWithText("Hard").performScrollTo()
        composeTestRule.onNodeWithText("Hard").performClick()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc)).performClick()
        composeTestRule.onNodeWithText("4h00").performScrollTo()
        composeTestRule.onNodeWithText("4h00").performClick()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc))
            .performTextInput("Look at sad ingredients\nsob in bowl to add salt\nmix together\nenjoy")
        composeTestRule.onNodeWithText("Done").performClick()
    }

    @Test
    fun ingredientsComposableIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }
        composeTestRule.onNodeWithStringId(R.string.RecipeCreationIngredientsTitle)
        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc)).assertIsDisplayed()
    }

    @Test
    fun recipeStepsComposableIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.RecipePreparationTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).assertIsDisplayed()
    }

    @Test
    fun recipeNameIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationRecipeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc))
    }

    @Test
    fun servingsComposableIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationScreenServingsTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc))
    }

    @Test
    fun cookingTimeComposableIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationCookingTimeEntryTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc))
    }

    @Test
    fun difficultyComposableIsDisplayed() {
        composeTestRule.setContent {
            RecipeCreationScreen(submitForm)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationDifficultyTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc))
    }

    private fun ComposeContentTestRule.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 10_000L
    ) {
        this.waitUntil {
            this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
