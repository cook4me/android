package ch.epfl.sdp.cook4me.ui.signup

import AddProfileInfoScreen
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

class AddProfileInfoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)
        val favFood = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)
        val blankUser = composeTestRule.activity.getString(R.string.invalid_username_message)

        val usernameInput = "donald"
        val favFoodInput = "pizza"
        val allergiesInput = "gluten"
        val bioInput = "I love cooking"

        composeTestRule.setContent { AddProfileInfoScreen(onSuccessfullSignUp = {}) }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(favFood).performTextClearance()

        composeTestRule.onNodeWithTag(saveBtn).performClick()
        composeTestRule.onNodeWithText(blankUser).assertIsDisplayed()
        // Set input mail
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)
        composeTestRule.onNodeWithTag(favFood).performTextInput(favFoodInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favFoodInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
    }

    @Test
    fun navigationTest() {
        var isClicked = false
        composeTestRule.setContent {
            AddProfileInfoScreen(onSuccessfullSignUp = { isClicked = true })
        }
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was not handled because no input
        assert(!isClicked)

        // Set input
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)

        val usernameInput = "donald"

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was handled
        assert(isClicked)
    }
}


