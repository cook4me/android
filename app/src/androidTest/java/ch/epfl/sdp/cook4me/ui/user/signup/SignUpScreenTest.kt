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
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val EMAIL_INPUT = "donald.duck@epfl.ch"
private const val PASSWORD_INPUT = "top secret"

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val store: FirebaseFirestore = setupFirestore()
    private val repository: ProfileRepository = ProfileRepository(store)

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)
        // val blankPassword = composeTestRule.activity.getString(R.string.password_blank)
        val blankMail = composeTestRule.activity.getString(R.string.invalid_email_message)

        composeTestRule.setContent {
            SignUpScreen(
                onSuccessfulSignUp = {},
                viewModel = SignUpViewModel(
                    repository = repository,
                    accountService = AccountService(auth),
                ),
            )
        }

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        composeTestRule.onNodeWithTag(saveBtn).performClick()
        composeTestRule.onNodeWithText(blankMail).assertIsDisplayed()
        // Set input mail
        composeTestRule.onNodeWithTag(email).performTextInput(EMAIL_INPUT)

        // test snackbar
        composeTestRule.onNodeWithTag(saveBtn).performClick()
        // composeTestRule.onNodeWithText(blankPassword).assertIsDisplayed() TODO : fix the snackbar

        // set input password
        composeTestRule.onNodeWithTag(password).performTextInput(PASSWORD_INPUT)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(EMAIL_INPUT).assertExists()

        // Check password field content is not displayed
        composeTestRule.onNodeWithText(PASSWORD_INPUT).assertDoesNotExist()
    }

    @Test
    fun navigationTest() {
        var isClicked = false
        composeTestRule.setContent {
            SignUpScreen(
                onSuccessfulSignUp = { isClicked = true },
                viewModel = SignUpViewModel(),
            )
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

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(email).performTextInput(EMAIL_INPUT)
        composeTestRule.onNodeWithTag(password).performTextInput(PASSWORD_INPUT)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was handled
        assert(isClicked)
    }
}
