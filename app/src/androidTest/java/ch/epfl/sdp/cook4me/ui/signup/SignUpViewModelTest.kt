package ch.epfl.sdp.cook4me.ui.signup

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.net.toUri
import ch.epfl.sdp.cook4me.ui.signUp.SignUpViewModel
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

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
