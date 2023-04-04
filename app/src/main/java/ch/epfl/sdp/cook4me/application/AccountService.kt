package ch.epfl.sdp.cook4me.application

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

class AccountService(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    private val _minPasswordLength = 6

    fun getCurrentUser(): FirebaseUser? =
        auth.currentUser

    fun getCurrentUserEmail(): String? {
        val currentUser: FirebaseUser? = getCurrentUser()
        if (currentUser == null) {
            throw NullPointerException("current user is null!")
        }
        return currentUser.email
    }
    suspend fun signOut(): Result<String> {
        return try {
            auth.signOut()
            /*
            Just signing out actually won't throw any exceptions,
            even when there is no user logged in.
            However if we need to do something else upon signing out,
            e.g. call another function like revokeAccessFromServer(),
            which could throw an exception, then we should wrap it in a try-catch block.
            */
            Result.success("Sign out successful")
        } catch (e: Exception) {
            Result.failure(e)
        }
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
