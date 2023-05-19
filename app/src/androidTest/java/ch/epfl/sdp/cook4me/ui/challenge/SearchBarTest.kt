package ch.epfl.sdp.cook4me.ui.challenge

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.challengefeed.SearchBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchBarTest_initialState() {
        composeTestRule.setContent {
            SearchBar(
                text = "",
                onTextChange = {}
            )
        }

        composeTestRule.onNodeWithText("Search...").assertExists()
        composeTestRule.onNodeWithContentDescription("Search Icon").assertDoesNotExist()
    }

    @Test
    fun searchBarTest_focusedState() {
        var text by mutableStateOf("")

        composeTestRule.setContent {
            SearchBar(
                text = text,
                onTextChange = { newText -> text = newText }
            )
        }

        composeTestRule.onNodeWithText("Search...").performClick()
        composeTestRule.onNodeWithText("Search...").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Search Icon").assertExists()
    }
}
