package ch.epfl.sdp.cook4me.ui.user.signup

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val accountService: AccountService = AccountService(),
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(),
) : ViewModel() {
    private var _password = mutableStateOf("")
    private var _formError = mutableStateOf(false)
    val formError: State<Boolean> = _formError

    private val _profileState = mutableStateOf(Profile())
    val profile: State<Profile> = _profileState

    private val _profileImage = mutableStateOf<Uri>(Uri.EMPTY)
    val profileImage: State<Uri> = _profileImage

    fun addPassword(password: String) {
        _password.value = password
    }

    fun addEmail(email: String) {
        _profileState.value.email = email
    }

    fun addAllergies(allergies: String) {
        _profileState.value.allergies = allergies
    }

    fun addBio(bio: String) {
        _profileState.value.bio = bio
    }

    fun addFavoriteDish(favoriteDish: String) {
        _profileState.value.favoriteDish = favoriteDish
    }

    fun addProfileImage(uri: Uri) {
        _profileImage.value = uri
    }

    fun onSubmit(
        onSignUpSuccess: () -> Unit,
        onSignUpFailure: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                accountService.register(_profileState.value.email, _password.value)
                if (profileImage.value != Uri.EMPTY) {
                    profileImageRepository.add(profileImage.value)
                }
                repository.add(_profileState.value)
            } catch (e: FirebaseAuthException) {
                if (e.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                    onSignUpFailure()
                } else {
                    throw e
                }
            }
            onSignUpSuccess()
        }
    }
}
