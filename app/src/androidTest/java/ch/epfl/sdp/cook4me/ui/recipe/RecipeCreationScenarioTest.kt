package ch.epfl.sdp.cook4me.ui.recipe

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import ch.epfl.sdp.cook4me.registryOwnerFactory
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import ch.epfl.sdp.cook4me.waitUntilDisplayed
import ch.epfl.sdp.cook4me.waitUntilExists
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
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

    private val mockRecipeRepository = mockk<RecipeRepository>(relaxed = true)

    private val testUri: Uri = Uri.parse(
        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.ic_user
    )

    private val expectedRecipe = Recipe(
        name = "Sad Pizza",
        ingredients = listOf("flour", "water", "salt"),
        recipeSteps = listOf("Look at sad ingredients", "sob in bowl to add salt", "mix together", "enjoy"),
        servings = 1,
        difficulty = "Hard",
        cookingTime = "4h00",
        photos = listOf()
    )

    @Test
    fun validRecipeFormIsCorrectlySubmitted() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwnerFactory(testUri)) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                CreateRecipeScreen(repository = mockRecipeRepository)
            }
        }
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc))
            .performTextInput(expectedRecipe.name)
        composeTestRule.onNodeWithTag("Add From Gallery Button").performClick()
        composeTestRule.waitUntilDisplayed(hasContentDescription("Selected Image"))
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc))
            .performTextInput(expectedRecipe.servings.toString())
        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc))
            .performTextInput(expectedRecipe.ingredients.reduce { x, y -> "$x\n$y" })
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc)).performScrollTo()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc)).performClick()
        composeTestRule.waitUntilExists(hasText(expectedRecipe.difficulty))
        composeTestRule.onNodeWithText(expectedRecipe.difficulty).performScrollTo()
        composeTestRule.onNodeWithText(expectedRecipe.difficulty).performClick()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc)).performClick()
        composeTestRule.onNodeWithText(expectedRecipe.cookingTime).performScrollTo()
        composeTestRule.onNodeWithText(expectedRecipe.cookingTime).performClick()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc))
            .performTextInput(expectedRecipe.recipeSteps.reduce { x, y -> "$x\n$y" })
        composeTestRule.onNodeWithText("Done").performClick()
        verify {
            runTest {
                mockRecipeRepository.add(
                    expectedRecipe,
                    testUri
                )
            }
        }
        confirmVerified(mockRecipeRepository)
    }

    @Test
    fun ingredientsComposableIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }
        composeTestRule.onNodeWithStringId(R.string.RecipeCreationIngredientsTitle)
        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc)).assertIsDisplayed()
    }

    @Test
    fun recipeStepsComposableIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }

        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.RecipePreparationTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).assertIsDisplayed()
    }

    @Test
    fun recipeNameIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationRecipeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc))
    }

    @Test
    fun servingsComposableIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationScreenServingsTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc))
    }

    @Test
    fun cookingTimeComposableIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationCookingTimeEntryTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc))
    }

    @Test
    fun difficultyComposableIsDisplayed() {
        composeTestRule.setContent {
            CreateRecipeScreen(repository = mockRecipeRepository)
        }

        composeTestRule.onNodeWithStringId(R.string.RecipeCreationDifficultyTitle).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc))
    }
}
