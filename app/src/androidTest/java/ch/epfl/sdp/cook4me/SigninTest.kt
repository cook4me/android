package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SigninTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>() // mainactivity? componentactivity?

    private val testTagEmailField = "EmailField"
    private val testTagPasswordField = "PasswordField"
    private val invalidEmail = "invalidemail"
    private val validEmail = "bababa@epfl.ch"

    @Test
    fun uiIsDisplayed() {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_top_bar_message).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTagEmailField).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTagPasswordField).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).assertIsDisplayed()
    }

    @Test
    fun invalidEmailTriggersMessageBar() {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput(invalidEmail)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.onNodeWithStringId(R.string.invalid_email_message).assertIsDisplayed()
    }
}