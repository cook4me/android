package ch.epfl.sdp.cook4me.ui.signup

import EditProfileScreen
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

        composeTestRule.onNodeWithStringId(R.string.tag_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_password).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_email).assertIsDisplayed()
    }

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.tag_username)
        val favoriteDish = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)

        val usernameInput = "John"
        val favoriteDishInput = "Spaghetti"
        val allergiesInput = "Hazelnut"
        val bioInput = "Gourmet"
        val emailInput = "edith.mozabique@epfl.ch"
        val paswwordInput = "123456"

        composeTestRule.setContent { SignUpScreen() }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favoriteDish).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favoriteDish).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)
        composeTestRule.onNodeWithTag(password).performTextInput(paswwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
        composeTestRule.onNodeWithText(email).assertExists()
        composeTestRule.onNodeWithText(password).assertExists()
    }

    @Test
    fun clickingSaveButton() {
        composeTestRule.setContent {
            SignUpScreen()
        }
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_save)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()
    }

    @Test
    fun clickingCancelButton() {
        composeTestRule.setContent {
            SignUpScreen()
        }
        // Find the cancel button by its content description
        val cancelBtn = composeTestRule.activity.getString(R.string.btn_cancel)

        // Click the cancel button
        composeTestRule.onNodeWithTag(cancelBtn).performClick()
    }
}
