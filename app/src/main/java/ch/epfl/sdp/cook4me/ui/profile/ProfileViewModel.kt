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
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
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
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(),
    onFailure: () -> Unit = {},
) : ViewModel() {
    private var _id = accountService.getCurrentUserWithEmail() // Email as id
    private val _formError = mutableStateOf(false)
    val isLoading = mutableStateOf(true) // not private for testing
    private val _profileState = mutableStateOf(Profile())
    val profile = _profileState
    private val _profileImage = mutableStateOf<Uri>(Uri.EMPTY)
    val profileImage: State<Uri> = _profileImage

    init {
        viewModelScope.launch {
            // run blocking  to wait for the profile to be loaded
            // therefore avoid race condition on the isloaded value
            var profile =
                accountService.getCurrentUserWithEmail()?.let { repository.getById(it) }

            // load the profile again if it is null
            while (profile == null) {
                @Suppress("MagicNumber")
                delay(2000) // Wait for 1 second before retrying
                profile =
                    accountService.getCurrentUserWithEmail()?.let { repository.getById(it) }
            }

            try {
                profile.let {
                    withContext(Dispatchers.Main) {
                        _profileState.value.email = it.email
                        _profileState.value.name = it.name
                        _profileState.value.allergies = it.allergies
                        _profileState.value.bio = it.bio
                        _profileState.value.favoriteDish = it.favoriteDish
                    }
                }
            } catch (e: NoSuchElementException) {
                if (e.message == "Collection is empty.") {
                    onFailure()
                } else {
                    throw e
                }
            }

            try {
                _profileImage.value = profileImageRepository.get()
            } catch (e: NoSuchElementException) {
                if (e.message == "Collection is empty.") {
                    _profileImage.value =
                        Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
                } else {
                    throw e
                }
            }

            isLoading.value = false
        }
    }

    fun addProfileImage(uri: Uri) {
        _profileImage.value = uri
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
                        )
                        onSuccessListener()
                        isLoading.value = false
                    }
                }
            }
        }
    }
}
