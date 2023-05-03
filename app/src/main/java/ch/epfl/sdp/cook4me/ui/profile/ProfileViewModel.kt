package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.ProfileService
import ch.epfl.sdp.cook4me.application.ProfileServiceWithRepository
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val service: ProfileService = ProfileServiceWithRepository(),
    private val accountService: AccountService = AccountService(),
) : ViewModel() {
    private var _id = accountService.getCurrentUserWithEmail() // Email as id
    private val _formError = mutableStateOf(false)
    val isLoading = mutableStateOf(true) // not private for testing
    val formError: State<Boolean> = _formError
    private val _profileState = mutableStateOf(Profile())
    val profile = _profileState

    init {
        viewModelScope.launch {
            var profile = accountService.getCurrentUserWithEmail()?.let { repository.getById(it) }

            // load the profile again if it is null
            while (profile == null) {
                @Suppress("MagicNumber")
                delay(2000) // Wait for 1 second before retrying
                profile = accountService.getCurrentUserWithEmail()?.let { repository.getById(it) }
            }

            profile.let {
                withContext(Dispatchers.Main) {
                    _profileState.value.email = it.email
                    _profileState.value.name = it.name
                    _profileState.value.allergies = it.allergies
                    _profileState.value.bio = it.bio
                    _profileState.value.favoriteDish = it.favoriteDish
                    _profileState.value.userImage = it.userImage
                    isLoading.value = false
                }
            }
        }
    }

    fun addUsername(username: String) {
        profile.value.name = username
    }

    fun addAllergies(allergies: String) {
        profile.value.allergies = allergies
    }

    fun addBio(bio: String) {
        profile.value.bio = bio
    }

    fun addFavoriteDish(favoriteDish: String) {
        profile.value.favoriteDish = favoriteDish
    }

    fun addUserImage(image: Uri) {
        profile.value.userImage = image.toString()
    }

    fun onSubmit(onSuccessListener: () -> Unit) {
        if (profile.value.name.isBlank()) { // TODO ADD SNEAK BAR FOR errors and add errors
            _formError.value = true
        } else {
            viewModelScope.launch {
                runBlocking {
                    _id?.let {
                        isLoading.value = true
                        service.submitForm(
                            it, // Email as id
                            profile.value.name,
                            profile.value.allergies,
                            profile.value.bio,
                            profile.value.favoriteDish,
                            profile.value.userImage
                        )
                        onSuccessListener()
                        isLoading.value = false
                    }
                }
            }
        }
    }
}
