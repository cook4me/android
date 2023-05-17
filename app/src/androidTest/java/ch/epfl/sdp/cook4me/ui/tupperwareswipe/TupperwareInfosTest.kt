package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import org.junit.Rule
import org.junit.Test

class TupperwareInfosTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val tupperware = Tupperware("Title", "Description", "User")

    @Test
    fun tupperwareInfosDisplaysCorrectData() {
        var navigateToProfileCalled = false

        composeTestRule.setContent {
            TupperwareInfos(
                tupperware = tupperware,
                onNavigateToProfileWithId = {
                    navigateToProfileCalled = true
                    tupperware.user
                }
            )
        }

        composeTestRule.onNodeWithText(tupperware.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(tupperware.description).assertIsDisplayed()
        composeTestRule.onNodeWithText("View Profile").assertIsDisplayed()

        // Check if navigation is called when "View Profile" is clicked
        composeTestRule.onNodeWithText("View Profile").performClick()
        assert(navigateToProfileCalled)
    }
}
