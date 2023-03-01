package ch.epfl.sdp.cook4me.ui

import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.ui.data.WelcomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WelcomeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()



    fun setName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }
}