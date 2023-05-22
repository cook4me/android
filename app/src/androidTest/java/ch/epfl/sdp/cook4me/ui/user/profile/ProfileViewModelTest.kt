package ch.epfl.sdp.cook4me.ui.user.profile

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class ProfileViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)
    private val repository: ProfileRepository = ProfileRepository(store)
    private var userImage: Uri =
        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_launcher_foreground")
    private val user = Profile(
        email = USERNAME,
        name = "Donald",
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
    fun testCheckLoadAndUpdate() {
        // create a profileViewModel
        val profileViewModel = ProfileViewModel()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        assert(profileViewModel.profile.value.name == user.name)
        assert(profileViewModel.profile.value.allergies == user.allergies)
        assert(profileViewModel.profile.value.favoriteDish == user.favoriteDish)
        assert(profileViewModel.profile.value.bio == user.bio)
        assert(profileViewModel.profile.value.email == user.email)
        assert(profileViewModel.profileImage.value == userImage)

        // create onSignUpFailure and onSignUpSuccess
        var isUpdateSuccess = false

        // check that that is not possible to sign up without an username
        profileViewModel.onSubmit(
            onSuccessListener = { isUpdateSuccess = true }
        )

        profileViewModel.addUsername(user.name)
        profileViewModel.addAllergies(user.allergies)
        profileViewModel.addFavoriteDish(user.favoriteDish)
        profileViewModel.addBio(user.bio)

        profileViewModel.onSubmit(
            onSuccessListener = { isUpdateSuccess = true },
        )

        // wait on signupSuccess
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            isUpdateSuccess
        }

        // check that the function was called correctly
        assert(isUpdateSuccess)
    }
}
