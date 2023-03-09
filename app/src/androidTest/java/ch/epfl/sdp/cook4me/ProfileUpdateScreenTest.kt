package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import ch.epfl.sdp.cook4me.ui.ProfileUpdateScreen
import org.junit.Rule
import org.junit.Test


class ProfileUpdateScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun newUserUsesApp() {
        composeTestRule.setContent {
            ProfileUpdateScreen()
        }

        composeTestRule.onNodeWithStringId(R.string.tag_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_username).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_bio).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.tag_allergies).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.default_allergies).assertIsDisplayed()

    }

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.tag_username)
        val favoriteDish = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        val usernameInput = "John"
        val favoriteDishInput = "Spaghetti"
        val allergiesInput = "Hazelnut"
        val bioInput = "Gourmet"

        composeTestRule.setContent { ProfileUpdateScreen() }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favoriteDish).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favoriteDish).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)

        //Wait ot be completed
        composeTestRule.waitForIdle()

        //Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
    }

    @Test
    fun clicking_save_button() {
        composeTestRule.setContent {
            ProfileUpdateScreen()
        }

        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_save)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()
    }

    @Test
    fun clicking_cancel_button() {
        composeTestRule.setContent {
            ProfileUpdateScreen()
        }

        // Find the cancel button by its content description
        val cancelBtn = composeTestRule.activity.getString(R.string.btn_cancel)

        // Click the cancel button
        composeTestRule.onNodeWithTag(cancelBtn).performClick()
    }
}