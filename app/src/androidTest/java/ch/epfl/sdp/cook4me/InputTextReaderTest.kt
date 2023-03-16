package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputTextReader
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
            InputTextReader(question = "question", label = "label",
                exampleText = "example", onTextChanged = {})
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("label").assertIsDisplayed()
        composeTestRule.onNodeWithText("example").assertIsDisplayed()
    }

    @Test
    fun onTextChangedIsCalledWhenTextIsChanged() {
        var text = ""
        composeTestRule.setContent {
            InputTextReader(question = "question", label = "label",
                exampleText = "example", onTextChanged = { text = it })
        }

        composeTestRule.onNodeWithText("example").performTextInput("new text")
        assert(text == "new text")
    }


}