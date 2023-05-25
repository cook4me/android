package ch.epfl.sdp.cook4me.application

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AccountService(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    private val _userEmail = MutableStateFlow<String?>(null)
    var userEmail = _userEmail.asStateFlow()

    init {
        auth.addAuthStateListener {
            _userEmail.value = it.currentUser?.email
        }
    }
    fun getCurrentUser(): FirebaseUser? =
        auth.currentUser

    fun getCurrentUserWithEmail(): String? {
        val currentUser: FirebaseUser? = getCurrentUser()
        return currentUser?.email
    }
    fun signOut() {
        auth.signOut()
    }
    suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun userAlreadyExists(email: String): Boolean {
        val signInMethods = auth.fetchSignInMethodsForEmail(email).await()
        return signInMethods.signInMethods?.size?.let {
            it > 0
        } ?: false
    }

    suspend fun register(email: String, password: String): AuthResult =
        auth.createUserWithEmailAndPassword(email, password).await()
}
