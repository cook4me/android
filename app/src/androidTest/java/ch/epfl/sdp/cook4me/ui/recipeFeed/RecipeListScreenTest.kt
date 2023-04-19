package ch.epfl.sdp.cook4me.ui.recipeFeed

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
class RecipeListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultRecipeListScreenIsDisplayed() {
        val mockList = listOf(
            Pair(Pair("id1",Recipe(name = "Test recipe 1", cookingTime = "10 min")), 1),
            Pair(Pair("id2",Recipe(name = "Test recipe 2", cookingTime = "20 min")), 2)
        )
        composeTestRule.setContent {
            RecipeListScreen(recipeList = mockList, onNoteUpdate = { _, _ -> })
        }

        composeTestRule.onNodeWithText("Test recipe 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Test recipe 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("20 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }
}