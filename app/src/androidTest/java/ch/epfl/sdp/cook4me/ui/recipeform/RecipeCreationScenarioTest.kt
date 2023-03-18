package ch.epfl.sdp.cook4me.ui.recipeform

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun recipeWithoutIngredientsListShouldNotBeSubmittable() {
        assert(false)
    }

    @Test
    fun recipeWithoutTitleListShouldNotBeSubmittable() {
        assert(false)
    }

    @Test
    fun recipeWithoutImageShouldNotBeSubmittable() {
        assert(false)
    }

    @Test
    fun recipeWithoutStepsShouldNotBeSubmittable() {
        assert(false)
    }

    @Test
    fun validRecipeFormIsCorrectlySubmitted() {
        assert(false)
    }

    @Test
    fun ingredientsComposableIsDisplayed() {
        assert(false)
    }

    @Test
    fun imageSelectorIsDisplay() {
        assert(false)
    }

    @Test
    fun recipeStepsComposableIsDisplayed() {
        assert(false)
    }

    @Test
    fun recipeDescriptionIsDisplayed() {
        assert(false)
    }

    @Test
    fun recipeTitleIsDisplayed() {
        assert(false)
    }

}