package ch.epfl.sdp.cook4me.ui.user.profile

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
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
class EditProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)
    private val repository: ProfileRepository = ProfileRepository(store)

    private var userImage: Uri =
        Uri.parse("android.resource://ch.epfl.sdp.cook4me/" + R.drawable.ic_user)
    private val user = Profile(
        email = "donald.duck@epfl.ch",
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
            userImage = profileImageRepository.add(userImage)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            repository.deleteAll()
            profileImageRepository.deleteImageForCurrentUser()
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun editScreenTestDisplayValues() {
        val profileViewModel = ProfileViewModel()

        // Set up the test and wait for the screen to be loaded
        val favFoodTag = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergiesTag = composeTestRule.activity.getString(R.string.tag_allergies)
        val bioTag = composeTestRule.activity.getString(R.string.tag_bio)

        // Set input
        // This test does not work because of some issue not finding the text fields
        // after the clearence of the text fields this happends on connected test
        // but not when the test is run on its own
        val favoriteDishInput = "Butterbeer"
        val allergiesInput = "Snails"
        val bioInput = "I'm just the friend of harry"

        // Set up the test
        composeTestRule.setContent {
            EditProfileScreen(
                viewModel = profileViewModel
            )
        }

        // Wait for the screen to be loaded
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Verify that the image is displayed
        // composeTestRule.onNodeWithTag("tag_defaultProfileImage").assertExists()
        composeTestRule.waitForIdle()

        // Clear fields
        composeTestRule.onNodeWithTag(favFoodTag).performTextClearance()
        composeTestRule.onNodeWithTag(bioTag).performTextClearance()
        composeTestRule.onNodeWithTag(allergiesTag).performTextClearance()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(favFoodTag)
                .fetchSemanticsNodes().size == 1
        }

        // Set input
        composeTestRule.onNodeWithTag(favFoodTag).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergiesTag).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bioTag).performTextInput(bioInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()

        // Click on the save button
        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
    }

    @Test
    fun editProfileScreenCancelButtonTest() {
        var isCancelledClicked = false
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            EditProfileScreen(
                onCancelListener = { isCancelledClicked = true },
                viewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is not clicked
        assert(!isCancelledClicked)

        // Click on the cancel button
        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()

        // Check that the cancel button is clicked
        assert(isCancelledClicked)
    }

    @Test
    fun editProfileScreenSaveButtonIsClicked() {
        var isSaveClicked = false
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            EditProfileScreen(
                onSuccessListener = { isSaveClicked = true },
                viewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is not clicked
        assert(!isSaveClicked)

        // Click on the cancel button
        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is clicked
        assert(isSaveClicked)
    }
}
