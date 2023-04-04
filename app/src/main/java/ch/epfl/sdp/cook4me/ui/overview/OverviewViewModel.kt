package ch.epfl.sdp.cook4me.ui.overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.AccountService

class OverviewViewModel(
    private val accountService: AccountService = AccountService()
) : ViewModel() {
    // navigationState = 0 -> no navigation
    // navigationState = 1 -> signed out, navigate to login screen
    private val _navigationState = mutableStateOf<Int>(0)
    val navigationState
        get() = _navigationState

    fun onSignOutButtonClicked() {
        accountService.signOut()
        _navigationState.value = 1
    }

    fun onDetailedEventButtonClicked() {
        ///TODO

    }
}
