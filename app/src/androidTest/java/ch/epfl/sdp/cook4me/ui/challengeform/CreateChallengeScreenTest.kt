package ch.epfl.sdp.cook4me.ui.challengeform

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class CreateChallengeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockChallengeService = mockk<ChallengeFormService>(relaxed = true)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun assertAllQuestionsAreDisplayed() {
        composeTestRule.setContent {
            CreateChallengeScreen(mockChallengeService)
        }
        composeTestRule.onNodeWithStringId(R.string.ask_challenge_name).assertExists()
        composeTestRule.onNodeWithStringId(R.string.ask_challenge_description).assertExists()
        composeTestRule.onNodeWithStringId(R.string.select_date_button).assertExists()
        composeTestRule.onNodeWithStringId(R.string.select_time_button).assertExists()
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).assertExists()
    }

    @Test
    fun submitIncorrectFormsShowsError() {
        composeTestRule.setContent {
            CreateChallengeScreen(mockChallengeService)
        }

        coEvery { mockChallengeService.submitForm(match { !it.isValidChallenge }) } returns "error"
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performScrollTo()
        composeTestRule.onNodeWithStringId(R.string.ButtonRowDone).performClick()
        composeTestRule.waitUntilExists(hasText("error"))
        coVerify { mockChallengeService.submitForm(match { !it.isValidChallenge }) }
    }

    @Test
    fun cancelButtonClickCallsOnCancel() {
        var onCancelCalled = false
        composeTestRule.setContent {
            CreateChallengeScreen(
                onCancelClick = {
                    onCancelCalled = true
                }
            )
        }

        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
        MatcherAssert.assertThat(onCancelCalled, Matchers.`is`(true))
    }
    private fun ComposeContentTestRule.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 5000
    ) {
        this.waitUntil(timeoutMillis) {
            this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
