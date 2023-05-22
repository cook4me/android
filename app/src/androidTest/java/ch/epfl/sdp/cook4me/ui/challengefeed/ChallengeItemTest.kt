package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ch.epfl.sdp.cook4me.ui.challenge.feed.ChallengeItem
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import org.junit.Rule
import org.junit.Test

class ChallengeItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testChallengeItemInitialization() {
        composeTestRule.setContent {
            Cook4meTheme {
                ChallengeItem(
                    challengeName = "Challenge 1",
                    creatorName = "Creator 1",
                    participantCount = 10,
                )
            }
        }

        composeTestRule.onNodeWithText("Challenge 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("by Creator 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("x 10").assertIsDisplayed()
    }

    @Test
    fun testChallengeItemClick() {
        var clicked = false

        composeTestRule.setContent {
            Cook4meTheme {
                ChallengeItem(
                    challengeName = "Challenge 1",
                    creatorName = "Creator 1",
                    participantCount = 10,
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Challenge 1").performClick()

        // Check that the item was clicked
        assert(clicked)
    }
}
