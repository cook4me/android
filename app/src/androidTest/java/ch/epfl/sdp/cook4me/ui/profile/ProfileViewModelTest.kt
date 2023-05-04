package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
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

class ProfileViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var profileImageRepository: ProfileImageRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: ProfileRepository
    private var userImage: Uri =
        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_launcher_foreground")
    private val user = Profile(
        email = "donald.duck@epfl.ch",
        name = "Donald",
        allergies = "Hazelnut",
        bio = "I am a duck",
        favoriteDish = "Spaghetti",
    )

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
        repository = ProfileRepository(store)
        auth = FirebaseAuth.getInstance()
        profileImageRepository = ProfileImageRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            repository.add(user)
            userImage = profileImageRepository.add(userImage)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            repository.delete(user.email)
            profileImageRepository.delete()
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.currentUser?.delete()
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
