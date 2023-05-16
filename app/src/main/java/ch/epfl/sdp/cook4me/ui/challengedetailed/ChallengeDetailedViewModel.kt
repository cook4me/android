package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge

class ChallengeDetailedViewModel : ViewModel() {
    private val _challenge = mutableStateOf<Challenge?>(null)
    val challenge: State<Challenge?> = _challenge

    private val challengeFormService = ChallengeFormService()

    suspend fun fetchChallenge(challengeId: String) {
        val challenge = challengeFormService.getChallengeWithId(challengeId)
        _challenge.value = challenge
    }

    fun addCurrentUserAsParticipant() {
        // TODO: Add current user as participant
    }
}
