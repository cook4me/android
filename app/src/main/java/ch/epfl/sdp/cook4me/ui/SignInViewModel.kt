package ch.epfl.sdp.cook4me.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.ui.data.AuthRepository
import ch.epfl.sdp.cook4me.ui.data.AuthRepositoryImpl
import ch.epfl.sdp.cook4me.util.Resource
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
):ViewModel() {

    val _googleState = mutableStateOf(GoogleSIgnInState())
    val googleState: State<GoogleSIgnInState> = _googleState


    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        repository.googleSignIn(credential).collect{result ->
            when(result) {
                is Resource.Success -> {
                    _googleState.value = GoogleSIgnInState(success = result.data)
                }
                is Resource.Loading -> {
                    _googleState.value = GoogleSIgnInState(loading = true)
                }
                is Resource.Error -> {
                    _googleState.value = GoogleSIgnInState(error = result.message!!)
                }
            }
        }
    }

}