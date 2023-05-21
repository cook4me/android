package ch.epfl.sdp.cook4me.ui.challengedetailed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth

class ChallengeDetailedViewModel : ViewModel() {
    private val _challenge = mutableStateOf<Challenge?>(null)
    val challenge: State<Challenge?> = _challenge

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    private val challengeFormService = ChallengeFormService()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val currentUserMail = firebaseAuth.currentUser?.email

    suspend fun fetchChallenge(challengeId: String) {
        Log.d("Debug", "challengeId = $challengeId")
        val challenge = challengeFormService.getChallengeWithId(challengeId)

        if (challenge?.participants?.get(currentUserMail) != null) {
            _successMessage.value = "You have joined the challenge!"
        }
        _challenge.value = challenge
    }

    suspend fun addCurrentUserAsParticipant(challengeId: String) {
        currentUserMail?.let { mail ->
            _challenge.value?.let { challenge ->
                val updatedParticipants =
                    challenge.participants.toMutableMap().apply { this[mail] = 0 }
                val updatedParticipantsIsVoted =
                    challenge.participantIsVoted.toMutableMap().apply { this[mail] = false }

                val updatedChallenge = challenge.copy(
                    participants = updatedParticipants,
                    participantIsVoted = updatedParticipantsIsVoted
                )

                _challenge.value = updatedChallenge
                _loading.value = true

                try {
                    challengeFormService.updateChallenge(challengeId, updatedChallenge)
                    _successMessage.value = "You have joined the challenge!"
                } catch (e: FirebaseException) {
                    _errorMessage.value = "Something went wrong when trying to join the challenge"
                    Log.e("Error when adding current user to challenge", e.message.toString())
                } finally {
                    _loading.value = false
                }
            }
        }
    }
}
