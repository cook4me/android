package ch.epfl.sdp.cook4me.ui.signout

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.Cook4MeApp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignOutTest {
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
            auth.createUserWithEmailAndPassword("darth.vader@epfl.ch", "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("obi.wan@epfl.ch", "123456").await()
            auth.currentUser?.delete()
            auth.signInWithEmailAndPassword("darth.vader@epfl.ch","123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun whenUserSignedOutAppNavigatesToLoginScreen() = runTest {
        auth.signInWithEmailAndPassword("obi.wan@epfl.ch","123456").await()
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.onNodeWithStringId(R.string.sign_out).performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(context.getString(R.string.Login_Screen_Tag))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.Login_Screen_Tag)).assertIsDisplayed()
        assertNull(auth.currentUser)
    }

    @Test
    fun testSwitchUserWithSignOut() = runTest {
        auth.signOut() // make sure no user is logged in
        composeTestRule.setContent {
            Cook4MeApp()
        }
        // now we should be in the login screen
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(context.getString(R.string.Login_Screen_Tag))
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(context.getString(R.string.Login_Screen_Tag)).assertIsDisplayed()

    }
}