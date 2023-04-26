package ch.epfl.sdp.cook4me.ui.recipe.feed

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoteIconTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultVoteIconIsDisplayed() {
        composeTestRule.setContent {
            VoteIcon(counterValue = 0)
        }

        composeTestRule.onNodeWithContentDescription("Upvote").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Downvote").assertIsDisplayed()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun pressUpvoteIncreasesCounter() {
        var counter = 0
        composeTestRule.setContent {
            VoteIcon(counterValue = 0, onChange = { counter += it })
        }

        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        assertThat(counter, `is`(1))
    }

    @Test
    fun pressDownvoteDecreasesCounter() {
        var counter = 0
        composeTestRule.setContent {
            VoteIcon(counterValue = 0, onChange = { counter += it })
        }

        composeTestRule.onNodeWithContentDescription("Downvote").performClick()
        composeTestRule.onNodeWithText("-1").assertIsDisplayed()

        assertThat(counter, `is`(-1))
    }

    @Test
    fun pressTwiceUpvoteGoesBackToDefault() {
        var counter = 0
        composeTestRule.setContent {
            VoteIcon(counterValue = 0, onChange = { counter += it })
        }

        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()

        assertThat(counter, `is`(0))
    }

    @Test
    fun pressUpvoteOnDownvotedButtonRemovesDownvote() {
        var counter = 0
        composeTestRule.setContent {
            VoteIcon(counterValue = 0, onChange = { counter += it })
        }

        composeTestRule.onNodeWithContentDescription("Downvote").performClick()
        composeTestRule.onNodeWithContentDescription("Upvote").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        assertThat(counter, `is`(1))
    }
}
