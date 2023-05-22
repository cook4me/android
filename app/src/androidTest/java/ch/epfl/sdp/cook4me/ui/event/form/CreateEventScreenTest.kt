package ch.epfl.sdp.cook4me.ui.event.form

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.EventFormService
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import ch.epfl.sdp.cook4me.waitUntilExists
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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

        composeTestRule.onNodeWithStringId(R.string.ask_event_name).assertExists()
        composeTestRule.onNodeWithStringId(R.string.ask_event_description).assertExists()
        composeTestRule.onNodeWithStringId(R.string.address_default_question).assertExists()
        composeTestRule.onNodeWithStringId(R.string.ask_event_visibility).assertExists()
        composeTestRule.onNodeWithStringId(R.string.select_date_button).assertExists()
        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertExists()

        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).assertExists()
    }

    @Test
    fun submitIncorrectFormsShowsError() {
        composeTestRule.setContent {
            CreateEventScreen(mockEventService)
        }

        coEvery { mockEventService.submitForm(match { !it.isValidEvent }) } returns "error"
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performClick()
        composeTestRule.waitUntilExists(hasText("error"))
        coVerify { mockEventService.submitForm(match { !it.isValidEvent }) }
    }

    fun cancelButtonClickCallsOnCancel() {
        var onCancelCalled = false
        composeTestRule.setContent {
            CreateEventScreen(
                onCancelClick = {
                    onCancelCalled = true
                }
            )
        }

        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
        assertThat(onCancelCalled, `is`(true))
    }
}
