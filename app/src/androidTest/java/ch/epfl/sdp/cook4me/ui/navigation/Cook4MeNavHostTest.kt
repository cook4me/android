package ch.epfl.sdp.cook4me.ui.navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.Cook4MeApp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.authenticatedStartScreen
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import waitUntilDisplayed
import waitUntilExists

@RunWith(AndroidJUnit4::class)
class Cook4MeNavHostTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var context: Context
    private lateinit var navController: NavHostController

    private val currentRoute: String?
        get() = navController.currentBackStackEntry?.destination?.route

    private val permissionStatusProvider = TestPermissionStatusProvider(
        initialPermissions = mapOf(
            "TestPermission1" to Pair(true, true),
            "TestPermission2" to Pair(true, true)
        )
    )
    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }

    private fun navigateElsewhere(route: String) {
        val differentRoute = Screen.values().first { it.name != route }.name
        navController.navigate(differentRoute)
    }

    private fun navigateToScreenXTest(
        destination: String,
        nodeOnlyInDestination: SemanticsNodeInteraction
    ) {
        if (currentRoute == destination) {
            navigateElsewhere(destination)
        }
        nodeOnlyInDestination.assertDoesNotExist()
        navController.navigate(destination)
        nodeOnlyInDestination.assertExists()
    }

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        }

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            Cook4MeNavHost(
                navController = navController,
                permissionProvider = permissionStatusProvider,
                startDestination = authenticatedStartScreen,
                onSuccessfulAuth = {}
            )
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
    fun navigateToTupperwareSwipeScreenTest() {
        navigateToScreenXTest(
            Screen.TupperwareSwipeScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.tupperware_swipe_screen_tag))
        )
    }

    @Test
    fun navigateToCreateTupperwareScreenTest() {
        navigateToScreenXTest(
            Screen.CreateTupperwareScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.create_tupper_screen_tag))
        )
    }

    @Test
    fun navigateToCreateRecipeScreenTest() {
        navigateToScreenXTest(
            Screen.CreateRecipeScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.create_recipe_screen_tag))
        )
    }

    @Test
    fun navigateToRecipeFeedTest() {
        navigateToScreenXTest(
            Screen.RecipeFeed.name,
            composeTestRule.onNodeWithTag(getString(R.string.recipe_feed_screen_tag))
        )
    }

    @Test
    fun navigateToEventScreenTest() {
        navigateToScreenXTest(
            Screen.Event.name,
            composeTestRule.onNodeWithTag(getString(R.string.event_screen_tag))
        )
    }

    @Test
    fun navigateToCreateEventScreenTest() {
        navigateToScreenXTest(
            Screen.CreateEventScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.create_event_screen_tag))
        )
    }

    @Test
    fun navigateToDetailedEventScreenTest() {
        navigateToScreenXTest(
            Screen.DetailedEventScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.detailed_event_screen_tag))
        )
    }

    @Test
    fun navigateToProfileScreenTest() {
        navigateToScreenXTest(
            Screen.ProfileScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.profile_screen_tag))
        )
    }

    @Test
    fun navigateToEditProfileScreenTest() {
        navigateToScreenXTest(
            Screen.EditProfileScreen.name,
            composeTestRule.onNodeWithTag(getString(R.string.edit_profile_screen_tag))
        )
    }
}