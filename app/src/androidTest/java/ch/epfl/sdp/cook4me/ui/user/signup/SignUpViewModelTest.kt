package ch.epfl.sdp.cook4me.ui.user.signup

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val USERNAME = "donald"
private const val PASSWORD = "123456"
private const val FAVORITE_DISH = "pizza"
private const val ALLERGIES = "gluten"
private const val BIO = "I love cooking"
private const val EMAIL = "donald.duck@epfl.ch"
@RunWith(AndroidJUnit4::class)
class SignUpViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val profileRepository: ProfileRepository = ProfileRepository(store)
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)

    // Set input
//
    private val userImage: Uri =
        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_launcher_foreground")
//

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword(EMAIL, PASSWORD).await()
            profileRepository.deleteAll()
            profileImageRepository.deleteImageForCurrentUser()
            auth.currentUser?.delete()?.await()
        }
    }

    @SuppressLint("AssertionSideEffect")
    @Test
    fun testCheckForm() {
        val signUpViewModel = SignUpViewModel()

        // check that its not valid before adding it
        assert(!signUpViewModel.checkForm())
        assert(!signUpViewModel.isValidUsername(USERNAME))
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

        signUpViewModel.addUsername(USERNAME)
        signUpViewModel.addAllergies(ALLERGIES)
        signUpViewModel.addFavoriteDish(FAVORITE_DISH)
        signUpViewModel.addEmail(EMAIL)
        signUpViewModel.addBio(BIO)
        signUpViewModel.addPassword(PASSWORD)
        signUpViewModel.addProfileImage(userImage)

        // check getters
        assert(signUpViewModel.profile.value.name == USERNAME)
        assert(signUpViewModel.profile.value.allergies == ALLERGIES)
        assert(signUpViewModel.profile.value.favoriteDish == FAVORITE_DISH)
        assert(signUpViewModel.profile.value.bio == BIO)
        assert(signUpViewModel.profile.value.email == EMAIL)

        // check that its valid after adding it all
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
        assertThat(profileViewModel.profile.value.name, `is`(USERNAME))
        assertThat(profileViewModel.profile.value.favoriteDish, `is`(FAVORITE_DISH))
        assertThat(profileViewModel.profile.value.allergies, `is`(ALLERGIES))
        assertThat(profileViewModel.profile.value.bio, `is`(BIO))
        assertThat(profileViewModel.profile.value.email, `is`(EMAIL))
    }

    @Test
    fun signupTwiceWithSameEmail() {
        val signUpViewModel = SignUpViewModel()

        signUpViewModel.addUsername(USERNAME)
        signUpViewModel.addAllergies(ALLERGIES)
        signUpViewModel.addFavoriteDish(FAVORITE_DISH)
        signUpViewModel.addEmail(EMAIL)
        signUpViewModel.addBio(BIO)
        signUpViewModel.addPassword(PASSWORD)

        // create onSignUpFailure and onSignUpSuccess
        var isSignUpFailed: Boolean
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
