package ch.epfl.sdp.cook4me.ui.user.login

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignInPersistTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun whenUserSignedInAppNavigatesToOverviewScreen() = runTest {
        auth.signInWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
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
    fun whenNoUserSignedInAppNavigatesToLoginScreen() = runTest {
        auth.signOut()
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(context.getString(R.string.Login_Screen_Tag))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.Login_Screen_Tag)).assertIsDisplayed()
    }
}
