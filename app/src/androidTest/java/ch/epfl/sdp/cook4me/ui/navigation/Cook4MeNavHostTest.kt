package ch.epfl.sdp.cook4me.ui.navigation

import RepositoryFiller
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import testProfile
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

    private fun setNavHostWithStartingScreen(
        startingScreen: String,
    ) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            Cook4MeNavHost(
                navController = navController,
                permissionProvider = permissionStatusProvider,
                startDestination = startingScreen,
                onSuccessfulAuth = {},
                isOnline = true
            )
        }
    }

    private fun testCreateElementNavigation(createButtonName: String, startDestination: Int, endDestination: Int) {
        // Go to create element screen
        composeTestRule.onNodeWithTag(getString(endDestination))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(createButtonName).performClick()
        composeTestRule.onNodeWithTag(getString(endDestination)).assertExists()
        // Go back
        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()
        composeTestRule.waitUntilExists(hasTestTag(getString(startDestination)))
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
        RepositoryFiller.setUpUser(testProfile, auth, store)
    }

    @After
    fun cleanUp() {
        RepositoryFiller.cleanUpUser(testProfile, auth, store)
    }

    @Test
    fun tupperwareSwipeScreenNavigationTesting() {
        setNavHostWithStartingScreen(Screen.TupperwareSwipeScreen.name)
        composeTestRule.onNodeWithTag(getString(R.string.tupperware_swipe_screen_tag)).assertExists()

        testCreateElementNavigation(
            createButtonName = "Create a new Tupperware",
            startDestination = R.string.tupperware_swipe_screen_tag,
            endDestination = R.string.create_tupper_screen_tag
        )
    }

    @Test
    fun recipeFeedNavigationTesting() {
        setNavHostWithStartingScreen(Screen.RecipeFeed.name)
        composeTestRule.onNodeWithTag(getString(R.string.recipe_feed_screen_tag)).assertExists()

        testCreateElementNavigation(
            createButtonName = "Create a new Recipe",
            startDestination = R.string.recipe_feed_screen_tag,
            endDestination = R.string.create_recipe_screen_tag
        )
    }

    @Test
    fun eventScreenNavigationTesting() {
        setNavHostWithStartingScreen(Screen.Event.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.event_screen_tag)))
    }

    @Test
    fun createEventScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.CreateEventScreen.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.create_event_screen_tag)))
    }

    @Test
    fun editProfileScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.EditProfileScreen.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.edit_profile_screen_tag)))
    }

    @Test
    fun signUpScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.SignUpScreen.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.sign_up_screen_tag)))
    }

    @Test
    fun loginScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.Login.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.login_screen_tag)))
    }

    @Test
    fun addProfileInfoScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.SignUpUserInfo.name)
        composeTestRule.waitUntilExists(hasTestTag(getString(R.string.add_profile_info_screen_tag)))
    }

    @Test
    fun profileScreenNavigationTest() {
        setNavHostWithStartingScreen(Screen.ProfileScreen.name)
        composeTestRule.onNodeWithTag(getString(R.string.profile_screen_tag)).assertExists()

        // TODO Edit Profile navigation
    }
}
