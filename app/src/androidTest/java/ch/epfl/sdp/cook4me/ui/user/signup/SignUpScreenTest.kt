package ch.epfl.sdp.cook4me.ui.user.signup

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
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)
        val blankPassword = composeTestRule.activity.getString(R.string.password_blank)
        val blankMail = composeTestRule.activity.getString(R.string.invalid_email_message)

        val emailInput = "donald.duck@epfl.ch"
        val passwordInput = "123456"

        composeTestRule.setContent { SignUpScreen(onSuccessfullSignUp = {}) }

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        composeTestRule.onNodeWithTag(saveBtn).performClick()
        composeTestRule.onNodeWithText(blankMail).assertIsDisplayed()
        // Set input mail
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)

        // test snackbar
        composeTestRule.onNodeWithTag(saveBtn).performClick()
        // composeTestRule.onNodeWithText(blankPassword).assertIsDisplayed() TODO : fix this

        // set input password
        composeTestRule.onNodeWithTag(password).performTextInput(passwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(emailInput).assertExists()

        // Check password field content is not displayed
        composeTestRule.onNodeWithText(passwordInput).assertDoesNotExist()
    }

    @Test
    fun navigationTest() {
        var isClicked = false
        composeTestRule.setContent {
            SignUpScreen(onSuccessfullSignUp = { isClicked = true })
        }
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was not handled because no input
        assert(!isClicked)

        // Set input
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)

        val emailInput = "donald.duck@epfl.ch"
        val paswwordInput = "123456"

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)
        composeTestRule.onNodeWithTag(password).performTextInput(paswwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was handled
        assert(isClicked)
    }
}
