package ch.epfl.sdp.cook4me.ui.overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val accountService: AccountService = AccountService()
) : ViewModel() {
    // navigationState = 0 -> no navigation
    // navigationState = 1 -> signed out, navigate to login screen
    private val _navigationState = mutableStateOf<Int>(0)
    val navigationState
        get() = _navigationState
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?>
        get() = _userEmail

    init {
        viewModelScope.launch {
            accountService.userEmail.collect { _userEmail.value = it }
        }
    }
    fun onSignOutButtonClicked() {
        accountService.signOut()
        _navigationState.value = 1
    }
}
