package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var repository: ProfileRepository
    private lateinit var profileImageRepository: ProfileImageRepository
    val profileImage = Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")

    private val user = Profile(
        email = "donald.duck@epfl.ch",
        name = "Donald",
        allergies = "Hazelnut",
        bio = "I am a duck",
        favoriteDish = "Spaghetti",
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
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
        profileImageRepository = ProfileImageRepository()
        runBlocking {
            auth.createUserWithEmailAndPassword(user.email, "123456").await()
            auth.signInWithEmailAndPassword(user.email, "123456").await()
            repository.add(user)
            profileImageRepository.add(profileImage)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            // delete the user from the database
            repository.delete(user.email)
            profileImageRepository.delete()
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun profileImageIsDisplayedTest() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        composeTestRule.onNodeWithTag("defaultProfileImage").assertExists()
    }

    @Test
    fun profileLoadCorrectValuesTest() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            ProfileScreen(
                profileViewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        composeTestRule.onNodeWithText(user.name).assertExists()
        composeTestRule.onNodeWithText(user.favoriteDish).assertExists()
        composeTestRule.onNodeWithText(user.allergies).assertExists()
        composeTestRule.onNodeWithText(user.bio).assertExists()
    }

    @Test
    fun profileScreenStateTest() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        composeTestRule.setContent { ProfileScreen(profileViewModel = profileViewModel) }

        profileViewModel.isLoading.value = true

        // Wait for a moment to allow Compose to recompose
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()

        profileViewModel.isLoading.value = false

        // Wait for a moment to allow Compose to recompose
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertDoesNotExist()
    }
}
