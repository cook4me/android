package ch.epfl.sdp.cook4me.ui.signup

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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

class SignUpViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var profileImageRepository: ProfileImageRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    // Set input
    private val username = "donald"
    private val favoriteDish = "pizza"
    private val allergies = "gluten"
    private val bio = "I love cooking"
    private val email = "donald.duck@epfl.ch"
    private val password = "123456"

    private val userImage: Uri =
        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_launcher_foreground")

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
            auth.signInWithEmailAndPassword(email, password).await()
            // delete collection from firebase
            store.collection(COLLECTION_PATH_PROFILES).whereEqualTo("email", email).get()
                .await().documents.forEach {
                    store.collection(COLLECTION_PATH_PROFILES).document(it.id).delete().await()
                }
            // delete profile image from firebase
            profileImageRepository.delete()

            // delete user from firebase
            auth.currentUser?.delete()
        }
    }

    @SuppressLint("AssertionSideEffect")
    @Test
    fun testCheckForm() {
        val signUpViewModel = SignUpViewModel()

        // check that its not valid before adding it
        assert(!signUpViewModel.checkForm())
        assert(!signUpViewModel.isValidUsername(username))
        assert(signUpViewModel.profile.value.name == "")

        // create onSignUpFailure and onSignUpSuccess
        var isSignUpFailed = false
        var isSignUpSuccess = false

        // check that that is not possible to sign up without an username
        signUpViewModel.onSubmit(
            onSignUpFailure = { isSignUpFailed = true },
            onSignUpSuccess = { isSignUpSuccess = true }
        )

        // check that the function was called correctly
        assert(signUpViewModel.formError.value)
        assert(isSignUpFailed)
        assert(!isSignUpSuccess)

        signUpViewModel.addUsername(username)
        signUpViewModel.addAllergies(allergies)
        signUpViewModel.addFavoriteDish(favoriteDish)
        signUpViewModel.addEmail(email)
        signUpViewModel.addBio(bio)
        signUpViewModel.addPassword(password)
        signUpViewModel.addProfileImage(userImage)

        // check getters
        assert(signUpViewModel.profile.value.name == username)
        assert(signUpViewModel.profile.value.allergies == allergies)
        assert(signUpViewModel.profile.value.favoriteDish == favoriteDish)
        assert(signUpViewModel.profile.value.bio == bio)
        assert(signUpViewModel.profile.value.email == email)
        //assert(signUpViewModel.profileImage.toString() == userImage.toString()) //TODO does assert an error user something

        // check that its valid after adding it
        assert(signUpViewModel.checkForm())
        assert(signUpViewModel.formError.value)

        isSignUpFailed = false
        isSignUpSuccess = false

        signUpViewModel.onSubmit(
            onSignUpFailure = { isSignUpFailed = true },
            onSignUpSuccess = { isSignUpSuccess = true }
        )

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            isSignUpSuccess
        }

        // check that the function was called correctly
        assert(!isSignUpFailed)
        assert(isSignUpSuccess)

        // check that the user is created correctly
        val profileViewModel = ProfileViewModel()

        // wait on profileViewModel
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // check that the user is created correctly
        assert(profileViewModel.profile.value.name == username)
        assert(profileViewModel.profile.value.favoriteDish == favoriteDish)
        assert(profileViewModel.profile.value.allergies == allergies)
        assert(profileViewModel.profile.value.bio == bio)
        assert(profileViewModel.profile.value.email == email)
    }

    @Test
    fun signupTwiceWithSameEmail() {
        val signUpViewModel = SignUpViewModel()

        signUpViewModel.addUsername(username)
        signUpViewModel.addAllergies(allergies)
        signUpViewModel.addFavoriteDish(favoriteDish)
        signUpViewModel.addEmail(email)
        signUpViewModel.addBio(bio)
        signUpViewModel.addPassword(password)

        // create onSignUpFailure and onSignUpSuccess
        var isSignUpFailed = false
        var isSignUpSuccess = false

        // 1. signup
        signUpViewModel.onSubmit(
            onSignUpFailure = { isSignUpFailed = true },
            onSignUpSuccess = { isSignUpSuccess = true }
        )

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            isSignUpSuccess
        }

        // reassign values
        isSignUpFailed = false
        isSignUpSuccess = false

        // 2. signup
        signUpViewModel.onSubmit(
            onSignUpFailure = { isSignUpFailed = true },
            onSignUpSuccess = { isSignUpSuccess = true }
        )

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            isSignUpFailed
        }
    }
}
