package ch.epfl.sdp.cook4me.ui.navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.Cook4MeApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import waitUntilExists

class NavigationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context

    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
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
    fun navigatingToTupperwareScreenFromBottomBar() {
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.onNodeWithText("nope").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tups").performClick()
        composeTestRule.waitUntilExists(hasText("nope"))
    }

    @Test
    fun navigatingToEventScreen() {
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.onNodeWithText("Tups").performClick()
        composeTestRule.onNodeWithText("EPFL").assertDoesNotExist()
        composeTestRule.onNodeWithText("Events").performClick()
        composeTestRule.waitUntilExists(hasText("EPFL"))
    }

    /*
    @Test
    fun navigatingToRecipes() {
        composeTestRule.setContent {
            Cook4MeApp()
        }
        composeTestRule.onNodeWithText("Top Recipes").assertDoesNotExist()
        composeTestRule.onNodeWithText("Recipes").performClick()
        composeTestRule.waitUntilExists(hasText("Top Recipes"))
    }
    */
}
