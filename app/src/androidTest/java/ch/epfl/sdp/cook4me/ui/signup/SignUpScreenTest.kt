package ch.epfl.sdp.cook4me.ui.signup

import SignUpScreen
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testDefaultValuesAreDisplayed() {
        composeTestRule.setContent {
            SignUpScreen()
        }

        composeTestRule.onNodeWithStringId(R.string.default_password).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_email).assertIsDisplayed()
    }

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)

        val emailInput = "edith.mozabique@epfl.ch"
        val paswwordInput = "123456"

        composeTestRule.setContent { SignUpScreen() }

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)
        composeTestRule.onNodeWithTag(password).performTextInput(paswwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(email).assertExists()
        composeTestRule.onNodeWithText(password).assertExists()
    }

    @Test
    fun clickingSaveButton() {
        composeTestRule.setContent {
            SignUpScreen()
        }
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()
    }

    @Test
    fun clickingCancelButton() {
        composeTestRule.setContent {
            SignUpScreen()
        }
        // Find the cancel button by its content description
        val cancelBtn = composeTestRule.activity.getString(R.string.btn_back)

        // Click the cancel button
        composeTestRule.onNodeWithTag(cancelBtn).performClick()
    }
}
