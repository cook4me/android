package ch.epfl.sdp.cook4me.ui.user.signup

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var firestore: FirebaseFirestore

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

    @Test
    fun testUserIsCorrect() {
        val signUpViewModel = SignUpViewModel()
        val username = "Donald Duck"

        // check that its not valid before adding it
        assert(!signUpViewModel.isValidUsername(username))

        // add the username
        signUpViewModel.addUsername(username)

        // check that its valid after adding it
        assert(signUpViewModel.isValidUsername(username))
    }

    @Test
    fun testCheckForm() {
        val signUpViewModel = SignUpViewModel()
        val username = "Donald Duck"
        val allergies = "Peanuts"
        val favoriteDish = "Pizza"
        val email = "donald.duck@epfl.ch"
        val password = "123456"
        val userImage = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"

        // check that its not valid before adding it
        assert(!signUpViewModel.checkForm())

        signUpViewModel.addUsername(username)
        signUpViewModel.addAllergies(allergies)
        signUpViewModel.addFavoriteDish(favoriteDish)
        signUpViewModel.addEmail(email)
        signUpViewModel.addPassword(password)
        signUpViewModel.addUserImage(userImage.toUri())

        // check that its valid after adding it
        assert(signUpViewModel.checkForm())

        signUpViewModel.onSubmit()
    }

    @Test
    fun checkGetters() {
        val signUpViewModel = SignUpViewModel()
        val username = "Donald Duck"
        val allergies = "Peanuts"
        val favoriteDish = "Pizza"
        val bio = "I am a duck"
        val formError = true

        signUpViewModel.checkForm()

        signUpViewModel.addUsername(username)
        signUpViewModel.addAllergies(allergies)
        signUpViewModel.addFavoriteDish(favoriteDish)
        signUpViewModel.addBio(bio)

        assert(signUpViewModel.username.value == username)
        assert(signUpViewModel.allergies.value == allergies)
        assert(signUpViewModel.favoriteDish.value == favoriteDish)
        assert(signUpViewModel.bio.value == bio)
        assert(signUpViewModel.formError.value == formError)
    }
}
