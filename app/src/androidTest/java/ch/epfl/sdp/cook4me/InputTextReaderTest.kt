package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputTextReader
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputTextReaderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun questionLabelAndExampleTextIsDisplayed() {
        composeTestRule.setContent {
            InputTextReader(
                question = "question", label = "label",
                onTextChanged = {}
            )
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("label").assertIsDisplayed()
    }

    @Test
    fun onTextChangedIsCalledWhenTextIsChanged() {
        var text = ""
        composeTestRule.setContent {
            InputTextReader(
                question = "question", label = "label",
                onTextChanged = { text = it }
            )
        }

        composeTestRule.onNodeWithText("").performTextInput("new text")
        assertEquals("new text", text)
    }
}
