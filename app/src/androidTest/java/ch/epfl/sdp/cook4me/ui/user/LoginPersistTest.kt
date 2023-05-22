package ch.epfl.sdp.cook4me.ui.user

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.Cook4MeApp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "obi.wan@epfl.ch"
private const val PASSWORD = "123456"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignInPersistTest {
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
    fun whenUserSignedInAppNavigatesToStartScreen() {
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Top recipes")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("Top recipes").assertIsDisplayed()
    }

    @Test
    fun whenNoUserSignedInAppNavigatesToLoginScreen() {
        auth.signOut()
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(context.getString(R.string.login_screen_tag))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.login_screen_tag)).assertIsDisplayed()
    }
}
