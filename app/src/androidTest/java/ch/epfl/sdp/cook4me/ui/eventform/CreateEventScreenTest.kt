package ch.epfl.sdp.cook4me.ui.eventform

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateEventScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockEventService = mockk<EventFormService>(relaxed = true)

    @Test
    fun assertAllQuestionsAreDisplayed() {
        composeTestRule.setContent {
            CreateEventScreen(mockEventService)
        }

        composeTestRule.onNodeWithStringId(R.string.ask_event_name).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.ask_event_description).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.address_default_question).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.ask_event_visibility).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.select_date_button).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).assertIsDisplayed()
    }

    @Test
    fun submitIncorrectFormsShowsError() {
        composeTestRule.setContent {
            CreateEventScreen(mockEventService)
        }

        coEvery { mockEventService.submitForm(match { !it.isValidEvent }) } returns "error"
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performClick()
        composeTestRule.onNodeWithText("error").assertIsDisplayed()
        coVerify { mockEventService.submitForm(match { !it.isValidEvent }) }
    }
}
