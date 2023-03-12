package ch.epfl.sdp.cook4me.ui

import androidx.lifecycle.ViewModel

class LoginViewModel (
    private val accountService: AccountService = AccountService(),
) : ViewModel() {

    suspend fun onSignInClick(email: String, password: String) {
            accountService.authenticate(email, password)
    }

    fun isEmailValid(email: String): Boolean {
        return accountService.isValidEmail(email)
    }

    fun isPasswordBlank(password: String): Boolean {
        return password.isBlank()
    }
}