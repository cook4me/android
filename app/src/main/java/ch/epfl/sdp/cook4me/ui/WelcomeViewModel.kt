package ch.epfl.sdp.cook4me.ui

import android.content.Context
import android.content.IntentSender
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.data.WelcomeUiState
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WelcomeViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
    fun setName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }
}