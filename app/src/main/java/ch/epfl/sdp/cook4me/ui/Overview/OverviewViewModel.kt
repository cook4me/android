package ch.epfl.sdp.cook4me.ui.Overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val accountService:AccountService = AccountService()
):ViewModel() {
    // navigationState = 0 -> no navigation
    // navigationState = 1 -> signed out, navigate to login screen
    private val _navigationState = mutableStateOf<Int>(0)
    val navigationState
        get() = _navigationState
    private val _signOutErrorMessage = mutableStateOf<String?>(null)
    val signOutErrorMessage
        get() = _signOutErrorMessage

    fun onSignOutButtonClicked() {
        viewModelScope.launch {
            val signOutResult = accountService.signOut()
            handleSignOutResult(signOutResult)
        }
    }

    private fun handleSignOutResult(signOutResult: Result<String>) {
        signOutResult.fold(
            onSuccess = {
                _navigationState.value = 1
            },
            onFailure = {exception ->
                _signOutErrorMessage.value = exception.message
            }
        )
    }
}