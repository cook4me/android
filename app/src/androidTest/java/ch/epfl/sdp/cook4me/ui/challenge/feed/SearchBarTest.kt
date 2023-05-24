package ch.epfl.sdp.cook4me.ui.challenge.feed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.isNotFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testSearchBarInitialization() {
        composeTestRule.setContent {
            Cook4meTheme {
                SearchBar(
                    text = "",
                    onTextChange = {},
                    onSearch = {},
                )
            }
        }
        composeTestRule.onNodeWithStringId(R.string.searchbar_placeholder_text).assertExists()
        composeTestRule.onNodeWithContentDescription("Search Icon").assertDoesNotExist()
    }

    @Test
    fun testTypingInSearchBar() {
        var searchText = ""

        composeTestRule.setContent {
            Cook4meTheme {
                SearchBar(
                    text = searchText,
                    onTextChange = {
                        searchText = it
                    },
                    onSearch = {},
                )
            }
        }

        composeTestRule.onNodeWithStringId(R.string.searchbar_placeholder_text).performTextInput("Hello")
        assertEquals(searchText, "Hello")
    }

    @Test
    fun testOnSearchCallback() {
        var searchText = ""
        var searchClicked = false

        composeTestRule.setContent {
            Cook4meTheme {
                SearchBar(
                    text = searchText,
                    onTextChange = {
                        searchText = it
                    },
                    onSearch = {
                        searchClicked = true
                    },
                )
            }
        }
        composeTestRule.onNodeWithStringId(R.string.searchbar_placeholder_text).performTextInput("Hello")
        composeTestRule.onNode(hasImeAction(ImeAction.Search)).performImeAction()

        assertThat(searchClicked, `is`(true))
        composeTestRule.onNodeWithTag("Search Field").assert(isNotFocused())
    }
}
