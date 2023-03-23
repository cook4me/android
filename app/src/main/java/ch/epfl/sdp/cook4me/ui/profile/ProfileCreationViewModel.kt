package ch.epfl.sdp.cook4me.ui.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.ProfileService
import ch.epfl.sdp.cook4me.application.TupperwareService
import ch.epfl.sdp.cook4me.ui.tupperwareform.MockTupperwareService
import kotlinx.coroutines.launch

class MockProfileService : ProfileService {
    override suspend fun submitForm(
        username: String,
        password: String,
        email: String,
        allergies: String,
        bio: String,
        favoriteDish: String,
        userImage: String,
        photos: List<String>
    ) {
        Log.d("Debug", "$username\n$email")
    }
}

class ProfileCreationViewModel(private val service: ProfileService = MockProfileService()) : ViewModel() {

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")
    private var _email = mutableStateOf("")
    private var _allergies = mutableStateOf("")
    private var _bio = mutableStateOf("")
    private var _favoriteDish = mutableStateOf("")
    private var _userImage = mutableStateOf("")
    private val _photos = mutableStateListOf<Uri>()
    private var _formError = mutableStateOf(false)

    val username: State<String> = _username
    val password: State<String> = _password
    val email: State<String> = _email
    val allergies: State<String> = _allergies
    val bio: State<String> = _bio
    val favoriteDish: State<String> = _favoriteDish
    val userImage: State<String> = _userImage
    val photos: List<Uri> = _photos
    val formError: State<Boolean> = _formError


    fun addUsername(username: String) {
        _username.value = username
    }

    fun addPassword(password: String) {
        _password.value = password
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

    fun addUserImage(image: String) {
        _userImage.value = image
    }

    fun addPhotos(uri: Uri) {
        _photos.add(uri)
    }

    // TODO implement tags

    fun onSubmit() {
        if (_username.value.isBlank() || _password.value.isBlank() ||  _email.value.isBlank() || _allergies.value.isBlank() || _bio.value.isBlank() || _favoriteDish.value.isBlank() ||_userImage.value.isBlank() ||  _photos.isEmpty()) {
            _formError.value = true
        } else {
            viewModelScope.launch {
                service.submitForm(
                    _username.value,
                    _password.value,
                    _email.value,
                    _allergies.value,
                    _bio.value,
                    _favoriteDish.value,
                    _userImage.value,
                    _photos.map { uri -> uri.toString() } // TODO pass actual images
                )
            }
        }
    }
}
