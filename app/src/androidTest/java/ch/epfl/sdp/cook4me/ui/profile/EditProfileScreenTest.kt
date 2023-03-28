package ch.epfl.sdp.cook4me.ui.profile

import EditProfileScreen
import androidx.activity.ComponentActivity
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

class EditProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testDefaultValuesAreDisplayed() {
        composeTestRule.setContent {
            EditProfileScreen(ProfileCreationViewModel())
        }
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

        composeTestRule.setContent { EditProfileScreen(ProfileCreationViewModel()) }

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

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
    }

    @Test
    fun clickingSaveButton() {
        composeTestRule.setContent {
            EditProfileScreen(ProfileCreationViewModel())
        }

        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
    }

    @Test
    fun clickingCancelButton() {
        composeTestRule.setContent {
            EditProfileScreen(ProfileCreationViewModel())
        }

        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
    }
}
