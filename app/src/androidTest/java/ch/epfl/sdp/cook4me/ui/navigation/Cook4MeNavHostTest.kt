package ch.epfl.sdp.cook4me.ui.navigation

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
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.TestPermissionStatusProvider
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import ch.epfl.sdp.cook4me.waitUntilExists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "jean.valejean@epfl.ch"
private const val PASSWORD = "123456"

@RunWith(AndroidJUnit4::class)
class Cook4MeNavHostTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val store: FirebaseFirestore = setupFirestore()
    private val profileRepository: ProfileRepository = ProfileRepository(store)
    private lateinit var navController: NavHostController

    val testProfile = Profile(
        email = "jean.valejean@epfl.ch",
        allergies = "reading and writing",
        bio = "I am miserable!",
        favoriteDish = "Bread"
    )

    @Before
    fun setUp() {
        runBlocking {
            profileRepository.add(testProfile)
            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            profileRepository.deleteAll()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD)
            auth.currentUser?.delete()?.await()
        }
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
}
