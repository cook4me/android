package ch.epfl.sdp.cook4me.ui.user.signup

import SignUpScreen
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

private const val VALID_USER = "valid.user@epfl.ch"
private const val VALID_PASSWORD = "123456"

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val accountService = AccountService(auth)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val context: Context = getInstrumentation().targetContext

    private lateinit var emailFieldTag: String
    private lateinit var passwordTag: String
    private lateinit var passwordRepeatTag: String
    private lateinit var continueButtonText: String
    private var successSignUpCalled = false

    @Before
    fun setup() {
        emailFieldTag = composeTestRule.activity.getString(R.string.tag_email)
        passwordTag = composeTestRule.activity.getString(R.string.tag_password)
        passwordRepeatTag = composeTestRule.activity.getString(R.string.tag_password_repeat)
        continueButtonText = composeTestRule.activity.getString(R.string.btn_continue)
        composeTestRule.setContent {
            SignUpScreen(accountService = accountService) { successSignUpCalled = true }
        }
    }


    @Test
    fun invalidEmailTest() {
        fillInFormAndTrySubmitting("invalid email", VALID_PASSWORD, VALID_PASSWORD)
        assertSnackbarMessage(R.string.invalid_email_message)
    }

    @Test
    fun emailAlreadyTakenTest() {
        val existingUser = "existing.user@epfl.ch"
        runBlocking {
            accountService.registerAndLogin(existingUser, VALID_PASSWORD)
        }
        fillInFormAndTrySubmitting(existingUser, VALID_PASSWORD, VALID_PASSWORD)
        assertSnackbarMessage(R.string.sign_up_screen_user_already_exists)
    }

    @Test
    fun existingUserFirstThenNewOneTest() {
        val existingUser = "existing.user@epfl.ch"
        runBlocking {
            accountService.registerAndLogin(existingUser, VALID_PASSWORD)
            auth.signOut()
        }
        fillInFormAndTrySubmitting(existingUser, VALID_PASSWORD, VALID_PASSWORD)
        clearEmailField()
        writeToEmailField(VALID_USER)
        clickButton()
        assertLoggedInWithUser(VALID_USER)
        runBlocking {
            deleteLoggedInUser() // delete newUser
            accountService.authenticate(existingUser, VALID_PASSWORD)
            deleteLoggedInUser() // delete existingUser
        }
    }

    @Test
    fun passwordFieldsDontMatchTest() {
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD, VALID_PASSWORD + "1")
        assertSnackbarMessage(R.string.sign_up_screen_password_not_identical)
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD + 1, VALID_PASSWORD)
        assertSnackbarMessage(R.string.sign_up_screen_password_not_identical)
    }


    @Test
    fun passwordFieldsDontMatchCorrectFirstTest() {
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD + 1, VALID_PASSWORD)
        clearPasswordField()
        writeToPasswordField(VALID_PASSWORD)
        clickButton()
        assertLoggedInWithUser(VALID_USER)
        runBlocking {
            deleteLoggedInUser() // delete newUser
        }
    }

    @Test
    fun passwordFieldsDontMatchCorrectSecondTest() {
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD, VALID_PASSWORD + 1)
        clearPasswordRepeatField()
        writeToPasswordRepeatedField(VALID_PASSWORD)
        clickButton()
        assertLoggedInWithUser(VALID_USER)
        runBlocking {
            deleteLoggedInUser() // delete newUser
        }
    }

    @Test
    fun passwordTooShortTest() {
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD.drop(1), VALID_PASSWORD.drop(1))
        assertSnackbarMessage(R.string.sign_up_screen_password_too_short)
    }

    @Test
    fun passwordTooShortSuccessfulAfterCorrectionTest() {
        fillInFormAndTrySubmitting(VALID_USER, VALID_PASSWORD.drop(1), VALID_PASSWORD.drop(1))
        writeToPasswordField(VALID_PASSWORD.last().toString())
        writeToPasswordRepeatedField(VALID_PASSWORD.last().toString())
        clickButton()
        assertLoggedInWithUser(VALID_USER)
        runBlocking {
            deleteLoggedInUser()
        }
    }

    private fun assertSnackbarMessage(resourceString: Int) {
        composeTestRule.onNodeWithText(context.getString(resourceString))
            .assertIsDisplayed()
    }

    private fun fillInFormAndTrySubmitting(
        email: String,
        password: String,
        passwordRepeated: String
    ) {
        clearEmailField()
        clearPasswordField()
        clearPasswordRepeatField()

        writeToEmailField(email)
        writeToPasswordField(password)
        writeToPasswordRepeatedField(passwordRepeated)
        clickButton()
    }

    private fun clearEmailField() =
        composeTestRule.onNodeWithTag(emailFieldTag).performTextClearance()

    private fun clearPasswordField() =
        composeTestRule.onNodeWithTag(passwordTag).performTextClearance()

    private fun clearPasswordRepeatField() =
        composeTestRule.onNodeWithTag(passwordRepeatTag).performTextClearance()

    private fun writeToEmailField(input: String) =
        composeTestRule.onNodeWithTag(emailFieldTag).performTextInput(input)

    private fun writeToPasswordField(input: String) =
        composeTestRule.onNodeWithTag(passwordTag).performTextInput(input)

    private fun writeToPasswordRepeatedField(input: String) =
        composeTestRule.onNodeWithTag(passwordRepeatTag).performTextInput(input)

    private fun clickButton() =
        composeTestRule.onNodeWithTag(continueButtonText).performClick()

    private fun assertLoggedInWithUser(user: String) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            successSignUpCalled
        }
        assertThat(accountService.getCurrentUser()?.email, `is`(user))
    }

    private suspend fun deleteLoggedInUser() =
        auth.currentUser?.delete()?.await()
}
