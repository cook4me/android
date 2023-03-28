package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.ProfileService
import ch.epfl.sdp.cook4me.application.ProfileServiceWithRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import kotlinx.coroutines.launch

class MockProfileService : ProfileService {
    override suspend fun submitForm(
        credentials: String,
        username: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
    ) {
        Log.d("Debug", "$credentials")
    }
}

class ProfileCreationViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val service: ProfileService = ProfileServiceWithRepository()
) : ViewModel() {

    init {
        viewModelScope.launch {
            val profile = repository.getByCredentials("1234")
            profile?.let {
                addAllergies(it.allergies)
                addFavoriteDish(it.favoriteDish)
                addBio(it.bio)
                addUsername(it.name)
                addUserImage(it.userImage.toUri())
            }
        }
    }

    private var _credentials = mutableStateOf("1234") // TODO ADD REAL CREDETIALS
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

    val credentials: State<String> = _credentials
    val username: State<String> = _username
    val allergies: State<String> = _allergies
    val bio: State<String> = _bio
    val favoriteDish: State<String> = _favoriteDish
    val userImage: State<Uri> = _userImage
    val formError: State<Boolean> = _formError

    fun addCredentials(credentials: String) {
        _credentials.value = credentials
    }

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

    // TODO implement tags
    fun onSubmit() {
        if (_credentials.value.isBlank() || _username.value.isBlank()) { // TODO MAKE CHECK COMPLETE
            _formError.value = true
        } else {
            viewModelScope.launch {
                service.submitForm(
                    _credentials.value,
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
