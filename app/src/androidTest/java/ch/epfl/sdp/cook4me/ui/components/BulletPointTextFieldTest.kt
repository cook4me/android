package ch.epfl.sdp.cook4me.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.BulletPointTextField
import ch.epfl.sdp.cook4me.ui.simpleComponent.GenericSeparators
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BulletPointTextFieldTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun inputTextIsCorrectlyDisplayed(
        separator: Sequence<String> = GenericSeparators.BulletSeparator,
        inputText: String,
        expectedText: String,
    ) {
        composeTestRule.setContent {
            BulletPointTextField(
                separators = separator, contentDescription = "TextField", onValueChange = {}
            )
        }
        val textField = composeTestRule.onNodeWithContentDescription("TextField")
        textField.performTextInput(inputText)
        textField.performClick()
        textField.assertTextEquals(expectedText)
    }

    @Test
    fun textIsDisplayedInCorrectFormat() {
        inputTextIsCorrectlyDisplayed(
            separator = GenericSeparators.BulletSeparator,
            inputText = "Hey,\nHow\nAre\nYou",
            expectedText = "• Hey,\n• How\n• Are\n• You"
        )
    }

    @Test
    fun textFieldIgnoresLeadingWhitespacesInBulletPoint() {
        inputTextIsCorrectlyDisplayed(
            separator = GenericSeparators.BulletSeparator,
            inputText = "    Hey,\n   How\n Are\n   You",
            expectedText = "• Hey,\n• How\n• Are\n• You",
        )
    }

    @Test
    fun textFieldDisplaysTextCorrectlyWithSeparatorsOfVaryingLength() {
        val varyingLengthSeparator = sequenceOf("1. ", "22. ", "333. ", "4444. ")
        inputTextIsCorrectlyDisplayed(
            separator = varyingLengthSeparator,
            inputText = "Hey,\nHow\nAre\nYou",
            expectedText = "1. Hey,\n22. How\n333. Are\n4444. You"
        )
    }
}
