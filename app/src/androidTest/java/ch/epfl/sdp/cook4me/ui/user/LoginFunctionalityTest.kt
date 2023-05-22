package ch.epfl.sdp.cook4me.ui.user

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.assertThrowsAsync
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "harry.potter@epfl.ch"
private const val PASSWORD = "123456"
private const val TEST_TAG_EMAIL_FIELD = "EmailField"
private const val TEST_TAG_PASSWORD_FIELD = "PasswordField"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignInFunctionalityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun validUserSignInSuccessfully() = runTest {
        auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
        assertThat(auth.currentUser?.email, `is`(USERNAME))
    }

    @Test
    fun invalidUserSignInTriggersException() =
        assertThrowsAsync {
            auth.signInWithEmailAndPassword("mrinvalid@epfl.ch", "hahaha").await()
        }

    @Test
    fun logInScreenValidUserWithCorrectPasswordSignInSuccessfully() {
        var wasCalled = false
        composeTestRule.setContent {
            LoginScreen { wasCalled = true }
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).performTextInput(USERNAME)
        composeTestRule.onNodeWithTag(TEST_TAG_PASSWORD_FIELD).performTextInput(PASSWORD)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            wasCalled
        }
        assertTrue(wasCalled)
    }

    @Test
    fun logInScreenNonExistUserSignInShowsNonExistUserMessage() {
        composeTestRule.setContent {
            LoginScreen {}
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).performTextInput("mr.nonexist@epfl.ch")
        composeTestRule.onNodeWithTag(TEST_TAG_PASSWORD_FIELD).performTextInput(PASSWORD)
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText(context.getString(R.string.sign_in_screen_non_exist_user))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_non_exist_user)).assertIsDisplayed()
    }

    @Test
    fun logInScreenValidUserWithWrongPasswordShowsWrongPasswordMessage() {
        composeTestRule.setContent {
            LoginScreen {}
        }
        composeTestRule.onNodeWithTag(TEST_TAG_EMAIL_FIELD).performTextInput(USERNAME)
        composeTestRule.onNodeWithTag(TEST_TAG_PASSWORD_FIELD).performTextInput("wrong password")
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button).performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText(context.getString(R.string.sign_in_screen_wrong_password))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_wrong_password)).assertIsDisplayed()
    }
}
