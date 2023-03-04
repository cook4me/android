package ch.epfl.sdp.cook4me.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.ui.data.AuthRepository
import ch.epfl.sdp.cook4me.util.Resource
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    val googleState_ = mutableStateOf(GoogleSIgnInState())
    val googleState: State<GoogleSIgnInState> = googleState_

    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        repository.googleSignIn(credential).collect{result ->
            when (result) {
                is Resource.Success -> {
                    googleState_.value = GoogleSIgnInState(success = result.data)
                }
                is Resource.Loading -> {
                    googleState_.value = GoogleSIgnInState(loading = true)
                }
                is Resource.Error -> {
                    googleState_.value = GoogleSIgnInState(error = result.message!!)
                }
            }
        }
    }
}

