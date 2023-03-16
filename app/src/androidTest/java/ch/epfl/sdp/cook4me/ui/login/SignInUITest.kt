package ch.epfl.sdp.cook4me.ui.login

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInUITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testTagEmailField = "EmailField"
    private val testTagPasswordField = "PasswordField"
    private val invalidEmail = "invalidemail"
    private val validEmail = "bababa@epfl.ch"
    private lateinit var context: Context
    @Before
    fun setUp() {
        context = getInstrumentation().targetContext
    }
    @Test
    fun uiIsDisplayed() {
        composeTestRule.setContent {
            LoginScreen {}
        }
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_top_bar_message).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTagEmailField).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTagPasswordField).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).assertIsDisplayed()
    }

    @Test
    fun invalidEmailTriggersInvalidEmailMessageBar() {
        composeTestRule.setContent {
            LoginScreen {}
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput(invalidEmail)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.invalid_email_message)).assertIsDisplayed()
    }

    @Test
    fun validEmailWithBlankPasswordTriggersBlankPasswordMessageBar() {
        composeTestRule.setContent {
            LoginScreen {}
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput(validEmail)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.password_blank)).assertIsDisplayed()
    }
}
