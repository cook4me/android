package ch.epfl.sdp.cook4me.ui.signup

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
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.ui.profile.ProfileViewModel
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val COLLECTION_PATH_PROFILES = "profiles"

class AddProfileInfoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var profileImageRepository: ProfileImageRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    // Set input
    private val usernameInput = "donald"
    private val favFoodInput = "pizza"
    private val allergiesInput = "gluten"
    private val bioInput = "I love cooking"
    private val emailInput = "donald.duck@epfl.ch"
    private val passwordInput = "123456"

    @Before
    fun setUp() {
        // Connect to local firestore emulator
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        storage = FirebaseStorage.getInstance()
        storage.useEmulator("10.0.2.2", 9199)
        auth = FirebaseAuth.getInstance()
        profileImageRepository = ProfileImageRepository(store, storage, auth)
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword(emailInput, passwordInput).await()
            // delete collection from firebase
            store.collection(COLLECTION_PATH_PROFILES).whereEqualTo("email", emailInput).get()
                .await().documents.forEach {
                    store.collection(COLLECTION_PATH_PROFILES).document(it.id).delete().await()
                }

            profileImageRepository.delete()
            // delete user from firebase
            auth.currentUser?.delete()
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
        signUpViewModel.addEmail(emailInput)
        signUpViewModel.addPassword(passwordInput)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()
        // Verify that the click was not handled because no input
        composeTestRule.onNodeWithText(blankUser).assertIsDisplayed()

        // Set input mail
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)
        composeTestRule.onNodeWithTag(favFood).performTextInput(favFoodInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favFoodInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()
        composeTestRule.onNodeWithTag("defaultProfileImage").assertDoesNotExist()

        // Click the save button this creates user in firebase
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            signUpSuccess
        }

        assert(!signUpFail)

        runBlocking {
            // check that the user is created correctly
            val profileViewModel = ProfileViewModel()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                !profileViewModel.isLoading.value
            }

            // check if the profile was stored correctly
            assert(profileViewModel.profile.value.name == usernameInput)
            assert(profileViewModel.profile.value.favoriteDish == favFoodInput)
            assert(profileViewModel.profile.value.allergies == allergiesInput)
            assert(profileViewModel.profile.value.bio == bioInput)

            // check that the user is created
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, passwordInput)
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
        signUpViewModel.addEmail(emailInput)
        signUpViewModel.addPassword(passwordInput)

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
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)

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
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, passwordInput)
                .await()
            assert(auth.currentUser != null)
        }
    }
}
