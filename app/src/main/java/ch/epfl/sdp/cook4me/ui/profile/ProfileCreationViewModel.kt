package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.ProfileService
import ch.epfl.sdp.cook4me.application.ProfileServiceWithRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileCreationViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val service: ProfileService = ProfileServiceWithRepository(),
    private val accountService: AccountService = AccountService(),
) : ViewModel() {

    init {
        viewModelScope.launch {
            val profile = accountService.getCurrentUserEmail()?.let { repository.getById(it) }
            profile?.let {
                addAllergies(it.allergies)
                addFavoriteDish(it.favoriteDish)
                addBio(it.bio)
                addUsername(it.name)
                addUserImage(it.userImage.toUri())
            }
        }
    }

    private var _id = accountService.getCurrentUserEmail() // Email as id
    private var _username = mutableStateOf("")
    private var _allergies = mutableStateOf("")
    private var _bio = mutableStateOf("")
    private var _favoriteDish = mutableStateOf("")
    private var _userImage = mutableStateOf<Uri>(
        Uri.parse(
            "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.ic_user
        )
    )
    private var _formError = mutableStateOf(false)

    val username: State<String> = _username
    val allergies: State<String> = _allergies
    val bio: State<String> = _bio
    val favoriteDish: State<String> = _favoriteDish
    val userImage: State<Uri> = _userImage
    val formError: State<Boolean> = _formError

    fun addUsername(username: String) {
        _username.value = username
    }

    fun addAllergies(allergies: String) {
        _allergies.value = allergies
    }

    fun addBio(bio: String) {
        _bio.value = bio
    }

    fun addFavoriteDish(favoriteDish: String) {
        _favoriteDish.value = favoriteDish
    }

    fun addUserImage(image: Uri) {
        _userImage.value = image
    }

    fun onSubmit() {
        if (_username.value.isBlank()) { // TODO ADD SNEAK BAR FOR errors and add errors
            _formError.value = true
        } else {
            viewModelScope.launch {
                _id?.let {
                    service.submitForm(
                        it,
                        _username.value,
                        _allergies.value,
                        _bio.value,
                        _favoriteDish.value,
                        _userImage.value.toString(),
                    )
                }
            }
        }
    }
}
