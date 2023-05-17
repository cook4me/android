package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class VoteScreenTest {
    @get:Rule
    var composeTestRule = createComposeRule()

    @Test
    fun ratingBarDisplaysCorrectNumberOfStars() {
        composeTestRule.setContent {
            val challenge = Challenge(
                name = "name",
                description = "description",
                dateTime = Calendar.getInstance(),
                participants = mapOf("participant1" to 0, "participant2" to 0, "participant3" to 0, "participant4" to 0, "participant5" to 0),
                creator = "darth.vader@epfl.ch",
                type = "French"
            )
            VotingScreen(
                challenge,
                {},
                {},
            )
        }

        // Assert that 5 stars are displayed
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 2")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 3")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 4")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 5")
            .assertIsDisplayed()

        // Assert that for all users are displayed
        composeTestRule.onNodeWithContentDescription("participant3 Empty star 5")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant4 Empty star 5")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant5 Empty star 5")
            .assertIsDisplayed()

        // Assert that 5 stars are displayed
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 2")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 3")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 4")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 5")
            .assertIsDisplayed()

        // Click on the 3rd star
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 3").performClick()
        // Click on the 5th star
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 5").performClick()

        // wait until idle
        composeTestRule.waitForIdle()

        // Assert that first 3 stars are filled
        composeTestRule.onNodeWithContentDescription("participant1 Star 1").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Star 2").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Star 3").assertIsDisplayed()

        // Assert that the last 2 stars are empty
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 4")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 5")
            .assertIsDisplayed()

        // Assert that 5 stars are filled
        composeTestRule.onNodeWithContentDescription("participant2 Star 1").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Star 2").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Star 3").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Star 4").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Star 5").assertIsDisplayed()

        // Click on the 5th star
        composeTestRule.onNodeWithContentDescription("participant2 Star 5").performClick()

        // wait until idle
        composeTestRule.waitForIdle()

        // assert that all stars are enmtpty
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 2")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 3")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 4")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant2 Empty star 5")
            .assertIsDisplayed()

        // Click on the 4th star
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 4").performClick()

        // wait until idle
        composeTestRule.waitForIdle()

        // Assert that first 3 stars are filled
        composeTestRule.onNodeWithContentDescription("participant1 Star 1").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Star 2").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Star 3").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("participant1 Star 4").assertIsDisplayed()

        // Assert that the last 2 stars are empty
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 5")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Vote").assertIsDisplayed()
    }

    @Test
    fun votingUpdatesChallengeCorrectly() {
        var updatedChallenge: Challenge? = null
        var isBackCancelClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                val challenge = Challenge(
                    name = "name",
                    description = "description",
                    dateTime = Calendar.getInstance(),
                    participants = mapOf("participant1" to 0, "participant2" to 0),
                    creator = "darth.vader@epfl.ch",
                    type = "French"
                )
                VotingScreen(
                    challenge,
                    { challenge ->
                        updatedChallenge = challenge
                    },
                    { isBackCancelClicked = true }
                )
            }
        }

        // Click on the 3rd star for participant1
        composeTestRule.onNodeWithContentDescription("participant1 Empty star 5").performClick()

        // Click on the vote button
        composeTestRule.onNodeWithText("Vote").performClick()

        // Assert that the returned Challenge has the correct star update
        assertEquals(5, updatedChallenge?.participants?.get("participant1"))

        // Click on cancel button
        composeTestRule.onNodeWithText("Cancel").performClick()
        assert(isBackCancelClicked)
    }
}
