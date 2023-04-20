package ch.epfl.sdp.cook4me.application

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AccountService(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    private val _minPasswordLength = 6
    private val _userEmail = MutableStateFlow<String?>(null)
    var userEmail = _userEmail.asStateFlow()

    init {
        auth.addAuthStateListener {
            _userEmail.value = it.currentUser?.email
        }
    }
    fun getCurrentUser(): FirebaseUser? =
        auth.currentUser

    fun getCurrentUserEmail(): String? {
        val currentUser: FirebaseUser? = getCurrentUser()
        return currentUser?.email
    }
    fun signOut() {
        auth.signOut()
    }
    suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String): Boolean =
        password.isNotBlank() && password.length >= _minPasswordLength

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }
}
