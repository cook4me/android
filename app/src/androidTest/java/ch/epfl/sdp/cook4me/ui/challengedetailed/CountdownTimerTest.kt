package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class CountdownTimerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assertTimeIsDisplayedBeforeEvent() {
        val dateTime = Calendar.getInstance().apply { add(Calendar.SECOND, 5) }

        composeTestRule.setContent {
            CountdownTimer(dateTime = dateTime)
        }

        composeTestRule.onNodeWithText("Time remaining: ").assertIsDisplayed()
    }

    @Test
    fun assertEvenHasTakenPlaceIsDisplayedAfterEvent() {
        val dateTime = Calendar.getInstance().apply { add(Calendar.SECOND, -5) }

        composeTestRule.setContent {
            CountdownTimer(dateTime = dateTime)
        }

        composeTestRule.onNodeWithText("This challenge has already taken place").assertIsDisplayed()
    }
}
