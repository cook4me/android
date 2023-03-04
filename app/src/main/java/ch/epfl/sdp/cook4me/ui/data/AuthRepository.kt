package ch.epfl.sdp.cook4me.ui.data

import ch.epfl.sdp.cook4me.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
}
