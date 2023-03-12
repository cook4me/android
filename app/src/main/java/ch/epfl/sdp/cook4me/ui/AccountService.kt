package ch.epfl.sdp.cook4me.ui

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AccountService (private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {


    suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}