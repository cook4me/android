package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var repository: ProfileRepository
    private val user = Profile(
        email = "donald.duck@epfl.ch",
        name = "Donald",
        allergies = "Hazelnut",
        bio = "I am a duck",
        favoriteDish = "Spaghetti",
        userImage = "Image of Donald",
        photos = listOf(""),
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
        runBlocking {
            try {
                auth.createUserWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
                auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
                repository.add(user)
            } catch (e: Exception) {
                // do nothing
            }
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            repository.delete(user.email)
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun testCheckLoadAndUpdate() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        assert(profileViewModel.profile.value.name == user.name)
        assert(profileViewModel.profile.value.allergies == user.allergies)
        assert(profileViewModel.profile.value.favoriteDish == user.favoriteDish)
        assert(profileViewModel.profile.value.bio == user.bio)
        assert(profileViewModel.profile.value.email == user.email)
        assert(profileViewModel.profile.value.userImage == user.userImage)

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
        profileViewModel.addUserImage(user.userImage.toUri())

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
