package ch.epfl.sdp.cook4me

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.ui.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.isA
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignInFunctionalityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private val testTagEmailField = "EmailField"
    private val testTagPasswordField = "PasswordField"
//TODO: restrict security rule only to these tests
//TODO: generalize helper function

    // special thanks to @bu-da for the kind helps
   @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }



    @Test
    fun validUserSignInSuccessfully() = runTest {
        auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        assertThat(auth.currentUser?.email, `is`("harry.potter@epfl.ch"))
    }

    @Test
    fun invalidUserSignInTriggersException() = runTest {
        assertThrowsAsync{
            auth.signInWithEmailAndPassword("mrinvalid@epfl.ch", "hahaha").await()
        }
    }

    @Test
    fun logInScreenValidUserWithCorrectPasswordSignInSuccessfully() = runTest {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("harry.potter@epfl.ch")
        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("123456")
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button)
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_sign_in_success))
    }

    @Test
    fun logInScreenNonExistUserSignInShowsNonExistUserMessage() = runTest {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("mr.nonexist@epfl.ch")
        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("123456")
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button)
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_non_exist_user))
    }

    @Test
    fun logInScreenValidUserWithWrongPasswordShowsWrongPasswordMessage() = runTest {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.onNodeWithTag(testTagEmailField).performTextInput("harry.potter@epfl.ch")
        composeTestRule.onNodeWithTag(testTagPasswordField).performTextInput("1234567")
        composeTestRule.onNodeWithStringId(R.string.sign_in_screen_sign_in_button)
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in_screen_wrong_password))
    }
}

//inline fun <reified T> assertThrowsAsync(crossinline f: suspend () -> Unit) {
//    try {
//        runBlocking {
//            f()
//        }
//    } catch (
//        e: Exception) {
//        if (e !is T) {
//            AssertionError("object is not of type ${T::class.java}")
//        }
//    }
//
//}

fun assertThrowsAsync(f: suspend () -> Unit) {
    try {
        runBlocking {
            f()
        }
    } catch (
        e: Exception) {
        return
    }
throw AssertionError("no exception was thrown")
}