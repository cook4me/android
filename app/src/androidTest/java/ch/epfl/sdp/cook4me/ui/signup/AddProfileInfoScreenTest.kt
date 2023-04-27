package ch.epfl.sdp.cook4me.ui.signup

import AddProfileInfoScreen
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.profile.ProfileViewModel
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
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

class AddProfileInfoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var firestore: FirebaseFirestore

    // Set input
    private val usernameInput = "donald"
    private val favFoodInput = "pizza"
    private val allergiesInput = "gluten"
    private val bioInput = "I love cooking"
    private val emailInput = "donald.duck@epfl.ch"
    private val passwordInput = "123456"
    private val COLLECTION_PATH = "profiles"

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
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    @After
    fun cleanUp() {
        runBlocking {
            try {
                // delete collection from firebase
                firestore.collection(COLLECTION_PATH).whereEqualTo("email", emailInput).get()
                    .await().documents.forEach {
                        firestore.collection(COLLECTION_PATH).document(it.id).delete().await()
                    }
                // check if the user exists already from a previous test
                // if a previous test failed, the user might still exist
                auth.signInWithEmailAndPassword(emailInput, passwordInput).await()
                auth.currentUser?.delete()
            } catch (e: Exception) {
                // do nothing
            }
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

        assert(profileViewModel.profile.value.name == usernameInput)
        assert(profileViewModel.profile.value.favoriteDish == favFoodInput)
        assert(profileViewModel.profile.value.allergies == allergiesInput)
        assert(profileViewModel.profile.value.bio == bioInput)

        runBlocking {
            // delete collection from firebase
            firestore.collection(COLLECTION_PATH).whereEqualTo("email", emailInput).get()
                .await().documents.forEach {
                    // check if the profile was stored correctly
                    assert(it["name"] == usernameInput)
                    assert(it["email"] == emailInput)
                    assert(it["favoriteDish"] == favFoodInput)
                    assert(it["allergies"] == allergiesInput)
                    assert(it["bio"] == bioInput)

                    firestore.collection(COLLECTION_PATH).document(it.id).delete().await()
                }

            // check that the user is created
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, passwordInput)
                .await()
            assert(auth.currentUser != null)

            // clean up
            auth.currentUser?.delete()
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

        // Add email and password to the view model from sign up screen
        signUpViewModel.addEmail(emailInput)
        signUpViewModel.addPassword(passwordInput)

        // Click the save button
        composeTestRule.onNodeWithTag(saveBtn).performClick()

        // Verify that the click was not handled because no input
        assert(!signUpSuccess)

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
            // delete collection from firebase
            firestore.collection(COLLECTION_PATH).whereEqualTo("email", emailInput).get()
                .await().documents.forEach {
                    firestore.collection(COLLECTION_PATH).document(it.id).delete().await()
                }

            // check that the user is created
            auth.signInWithEmailAndPassword(signUpViewModel.profile.value.email, passwordInput)
                .await()
            assert(auth.currentUser != null)

            // clean up
            auth.currentUser?.delete()
        }
    }
}
