package ch.epfl.sdp.cook4me.application

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AccountService(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    private val _minPasswordLength = 6

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
