package ch.epfl.sdp.cook4me.ui.user.profile

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "donald.duck@epfl.ch"
private const val PASSWORD = "123456"

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val repository: ProfileRepository = ProfileRepository(store)
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)
    private val context: Context = getInstrumentation().targetContext
    private val profileImage = Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/" + R.drawable.ic_user)
    private val user = Profile(
        email = USERNAME,
        allergies = "Hazelnut",
        bio = "I am a duck",
        favoriteDish = "Spaghetti",
    )

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            repository.add(user)
            profileImageRepository.add(profileImage)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            // delete the user from the database
            repository.deleteAll()
            profileImageRepository.deleteImageForCurrentUser()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
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

        composeTestRule.onNodeWithText(user.favoriteDish).assertExists()
        composeTestRule.onNodeWithText(user.allergies).assertExists()
        composeTestRule.onNodeWithText(user.bio).assertExists()
    }
}
