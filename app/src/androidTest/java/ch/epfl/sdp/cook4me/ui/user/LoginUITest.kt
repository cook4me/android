package ch.epfl.sdp.cook4me.ui.user

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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_TAG_EMAIL_FIELD = "EmailField"
private const val TEST_TAG_PASSWORD_FIELD = "PasswordField"

private const val INVALID_EMAIL = "invalidemail"
private const val VALID_EMAIL = "bababa@epfl.ch"

@RunWith(AndroidJUnit4::class)
class SignInUITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = getInstrumentation().targetContext

    @Test
    fun uiIsDisplayed() {
        composeTestRule.setContent {
            LoginScreen({}, {})
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_PASSWORD_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).assertIsDisplayed()
    }

    @Test
    fun invalidEmailTriggersInvalidEmailMessageBar() {
        composeTestRule.setContent {
            LoginScreen({}, {})
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).performTextInput(INVALID_EMAIL)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.invalid_email_message)).assertIsDisplayed()
    }

    @Test
    fun validEmailWithBlankPasswordTriggersBlankPasswordMessageBar() {
        composeTestRule.setContent {
            LoginScreen({}, {})
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).performTextInput(VALID_EMAIL)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.password_blank)).assertIsDisplayed()
    }
}
