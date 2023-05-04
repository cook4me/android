package ch.epfl.sdp.cook4me.ui.signup

import SignUpScreen
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var repository: ProfileRepository
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        /*
        * IMPORTANT:
        * (Below code is already functional, no need to change anything)
        * Make sure you do this try-catch block,
        * otherwise when doing CI, there will be an exception:
        * kotlin.UninitializedPropertyAccessException: lateinit property firestore has not been initialized
        * */
        try {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        repository = ProfileRepository()
        auth = FirebaseAuth.getInstance()
    }

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val email = composeTestRule.activity.getString(R.string.tag_email)
        val password = composeTestRule.activity.getString(R.string.tag_password)
        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)
        // val blankPassword = composeTestRule.activity.getString(R.string.password_blank)
        val blankMail = composeTestRule.activity.getString(R.string.invalid_email_message)

        val emailInput = "donald.duck@epfl.ch"
        val passwordInput = "123456"

        composeTestRule.setContent {
            SignUpScreen(
                onSuccessfulSignUp = {},
                viewModel = SignUpViewModel(
                    repository  = repository,
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
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)

        // test snackbar
        composeTestRule.onNodeWithTag(saveBtn).performClick()
        // composeTestRule.onNodeWithText(blankPassword).assertIsDisplayed() TODO : fix the snackbar

        // set input password
        composeTestRule.onNodeWithTag(password).performTextInput(passwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(emailInput).assertExists()

        // Check password field content is not displayed
        composeTestRule.onNodeWithText(passwordInput).assertDoesNotExist()
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

        val emailInput = "donald.duck@epfl.ch"
        val paswwordInput = "123456"

        // Clear fields
        composeTestRule.onNodeWithTag(email).performTextClearance()
        composeTestRule.onNodeWithTag(password).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(email).performTextInput(emailInput)
        composeTestRule.onNodeWithTag(password).performTextInput(paswwordInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was handled
        assert(isClicked)
    }
}
