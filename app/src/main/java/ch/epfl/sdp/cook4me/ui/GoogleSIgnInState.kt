package ch.epfl.sdp.cook4me.ui

import com.google.firebase.auth.AuthResult

data class GoogleSIgnInState(
    val success: AuthResult? = null,
    val loading: Boolean = false,
    val error: String = ""
)
