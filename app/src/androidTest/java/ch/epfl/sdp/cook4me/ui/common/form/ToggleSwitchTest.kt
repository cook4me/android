package ch.epfl.sdp.cook4me.ui.common.form

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToggleSwitchTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            ToggleSwitch(
                question = "question",
                answerChecked = "option1",
                answerUnchecked = "option2",
                onToggle = {}
            )
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("option1").assertIsDisplayed()
    }

    @Test
    fun onToggleIsCalledWhenSwitchIsPressed() {
        var toggle = true
        composeTestRule.setContent {
            ToggleSwitch(
                question = "question",
                answerChecked = "option1",
                answerUnchecked = "option2",
                onToggle = { toggle = it }
            )
        }

        composeTestRule.onNodeWithTag("switch").performClick()
        assert(!toggle)
        composeTestRule.onNodeWithTag("switch").performClick()
        assert(toggle)
    }
}
