package ch.epfl.sdp.cook4me.ui.user.signup

import AddProfileInfoScreen
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import ch.epfl.sdp.cook4me.ui.user.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val EMAIL_INPUT = "donald.duck@epfl.ch"
private const val PASSWORD_INPUT = "123456"

private const val USERNAME_INPUT = "donald"
private const val FAVFOOD_INPUT = "pizza"
private const val ALLERGIES_INPUT = "gluten"
private const val BIO_INPUT = "I love cooking"

@RunWith(AndroidJUnit4::class)
class AddProfileInfoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)

    // Set input

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword(EMAIL_INPUT, PASSWORD_INPUT).await()
            // delete collection from firebase
            val profileRepository = ProfileRepository()
            profileRepository.deleteAll()
            profileImageRepository.deleteImageForCurrentUser()
            // delete user from firebase
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun testTextFieldsInput() {
        // Set up the test
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)
        val favFood = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)
        val blankUser = composeTestRule.activity.getString(R.string.invalid_username_message)

        // Set up the view model
        val signUpViewModel = SignUpViewModel()

        // create onSigUpFailure and onSignUpSuccess
        var signUpSuccess = false
        var signUpFail = false

        // Set the content of the ComposeTestRule
        composeTestRule.setContent {
            AddProfileInfoScreen(
                onSuccessfulSignUp = { signUpSuccess = true },
                viewModel = signUpViewModel,
                onSignUpFailure = { signUpFail = true }
            )
        }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(favFood).performTextClearance()
        signUpViewModel.addProfileImage(Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user"))

        // Add email and password to the view model from sign up screen
        signUpViewModel.addEmail(EMAIL_INPUT)
        signUpViewModel.addPassword(PASSWORD_INPUT)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()
        // Verify that the click was not handled because no input
        composeTestRule.onNodeWithText(blankUser).assertIsDisplayed()

        // Set input mail
        composeTestRule.onNodeWithTag(username).performTextInput(USERNAME_INPUT)
        composeTestRule.onNodeWithTag(allergies).performTextInput(ALLERGIES_INPUT)
        composeTestRule.onNodeWithTag(bio).performTextInput(BIO_INPUT)
        composeTestRule.onNodeWithTag(favFood).performTextInput(FAVFOOD_INPUT)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(USERNAME_INPUT).assertExists()
        composeTestRule.onNodeWithText(FAVFOOD_INPUT).assertExists()
        composeTestRule.onNodeWithText(ALLERGIES_INPUT).assertExists()
        composeTestRule.onNodeWithText(BIO_INPUT).assertExists()
        composeTestRule.onNodeWithTag("defaultProfileImage").assertDoesNotExist()

        // Click the save button this creates user in firebase
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            signUpSuccess
        }

        assert(!signUpFail)

        // check that the user is created correctly
        val profileViewModel = ProfileViewModel()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // check if the profile was stored correctly
        assert(profileViewModel.profile.value.name == USERNAME_INPUT)
        assert(profileViewModel.profile.value.favoriteDish == FAVFOOD_INPUT)
        assert(profileViewModel.profile.value.allergies == ALLERGIES_INPUT)
        assert(profileViewModel.profile.value.bio == BIO_INPUT)

        runBlocking {
            // check that the user is created
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, PASSWORD_INPUT)
                .await()
            assert(auth.currentUser != null)
        }
    }

    @Test
    fun navigationTest() {
        var signUpSuccess = false
        var signUpFail = false
        // Set up the view model
        val signUpViewModel = SignUpViewModel()

        // Find the save button by its content description
        val saveBtn = composeTestRule.activity.getString(R.string.btn_continue)

        composeTestRule.setContent {
            AddProfileInfoScreen(
                onSuccessfulSignUp = { signUpSuccess = true },
                viewModel = signUpViewModel,
                onSignUpFailure = { signUpFail = true }
            )
        }

        // add userImage
        signUpViewModel.addProfileImage(Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user"))

        // Add email and password to the view model from sign up screen
        signUpViewModel.addEmail(EMAIL_INPUT)
        signUpViewModel.addPassword(PASSWORD_INPUT)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was not handled because no input
        assert(!signUpSuccess)
        assert(!signUpFail)

        // Set input
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(USERNAME_INPUT)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Click the save this creates user in firebaseEmulator
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            signUpSuccess
        }

        runBlocking {
            // check that the user is created
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, PASSWORD_INPUT)
                .await()
            assert(auth.currentUser != null)
        }
    }
}
