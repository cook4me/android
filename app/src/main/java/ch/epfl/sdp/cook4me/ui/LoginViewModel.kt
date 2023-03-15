package ch.epfl.sdp.cook4me.ui

import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.AccountService

class LoginViewModel(
    private val accountService: AccountService = AccountService(),
) : ViewModel() {

    suspend fun onSignInClick(email: String, password: String) {
        accountService.authenticate(email, password)
    }

    fun isEmailValid(email: String): Boolean =
        accountService.isValidEmail(email)

    fun isPasswordBlank(password: String): Boolean =
        password.isBlank()
}
