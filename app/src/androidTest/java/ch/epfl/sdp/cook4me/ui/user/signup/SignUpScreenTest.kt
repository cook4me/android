package ch.epfl.sdp.cook4me.ui.user.signup

import SignUpScreen
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ch.epfl.sdp.cook4me.R

const val VALID_PASSWORD = "123456"

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val auth: FirebaseAuth = setupFirebaseAuth()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val emailFieldTag = composeTestRule.activity.getString(R.string.tag_email)
    private val passwordTag = composeTestRule.activity.getString(R.string.tag_password)
    private val passwordRepeatTag = composeTestRule.activity.getString(R.string.tag_password_repeat)
    private val context: Context = getInstrumentation().targetContext

    @Test
    fun emailAlreadyTakenTest() {
        composeTestRule.setContent {
            SignUpScreen {
            }
        }
        composeTestRule.onNodeWithTag(emailFieldTag).performTextClearance()
        composeTestRule.onNodeWithTag(passwordTag).performTextClearance()
        composeTestRule.onNodeWithTag(passwordRepeatTag).performTextClearance()

        composeTestRule.onNodeWithTag(emailFieldTag).performTextInput("invalid email")
        composeTestRule.onNodeWithTag(passwordTag).performTextInput(VALID_PASSWORD)
        composeTestRule.onNodeWithTag(passwordRepeatTag).performTextInput(VALID_PASSWORD)
        composeTestRule.onNodeWithText(context.getString(R.string.invalid_email_message)).assertIsDisplayed()
    }


}
