package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.application.EventFormService

class ChallengeDetailedViewModel(
    challengeId: String,
    challengeService: EventFormService = EventFormService(),
) : ViewModel()
