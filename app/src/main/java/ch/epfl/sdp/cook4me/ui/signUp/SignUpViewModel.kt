package ch.epfl.sdp.cook4me.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.AccountService
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private lateinit var accountService: AccountService
    //vprivate lateinit var credential: AuthCredential

    fun onCreate(email: String, password: String) {
        accountService = AccountService()

        // Authenticate the user with their email and password
        viewModelScope.launch {
            try {
                accountService.authenticate(email, password)
                // Authentication successful, do something here
            } catch (e: Exception) {
                // Authentication failed, handle the error here
            }
        }
    }
}
