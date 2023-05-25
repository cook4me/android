package ch.epfl.sdp.cook4me.ui.challenge.feed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
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
                    joined = false
                )
            }
        }

        composeTestRule.onNodeWithText("Challenge 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("by Creator 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("x 10").assertIsDisplayed()
    }

    @Test
    fun whenUserHasJoinedChallengeJoinedShouldBeDisplayed() {
        composeTestRule.setContent {
            Cook4meTheme {
                ChallengeItem(
                    challengeName = "Challenge 1",
                    creatorName = "Creator 1",
                    participantCount = 10,
                    onClick = {},
                    joined = true
                )
            }
        }

        composeTestRule.onNodeWithText("JOINED").assertExists()
    }
}
