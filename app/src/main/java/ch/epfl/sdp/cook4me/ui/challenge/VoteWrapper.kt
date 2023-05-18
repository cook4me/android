import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challenge.RankingScreen
import ch.epfl.sdp.cook4me.ui.challenge.VotingScreen
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.challengeform.addParticipant
import ch.epfl.sdp.cook4me.ui.challengeform.changeParticipantScore
import ch.epfl.sdp.cook4me.ui.chat.LoadingScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun VoteWrapper(
    service: ChallengeFormService = ChallengeFormService(),
    challengeId: String,
    currentUser: String,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var challenge by remember { mutableStateOf<Challenge?>(null) }
    var challengeAfterVote by remember { mutableStateOf<Challenge?>(null) }
    var challengeVote by remember { mutableStateOf<Challenge?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isVoting by remember { mutableStateOf(false) }
    var hasVoted by remember { mutableStateOf(false) }
    var scoreOfCurrentUser by remember { mutableStateOf(0) }
    var alreadyVoted by remember { mutableStateOf(false) }

    LaunchedEffect(challengeId, isLoading, isVoting, hasVoted, challengeVote) {
        if (isLoading) {
            withContext(context = coroutineScope.coroutineContext) {
                challenge = service.getChallengeWithId(challengeId)
            }

            // Check if current user has already voted and if so disable voting
            challenge?.let {
                val hasVoted = it.participantIsVoted.getOrDefault(currentUser, false)
                if (hasVoted) {
                    alreadyVoted = true // Set alreadyVoted to true when the user has already voted
                } else {
                    // Remove current user's score from challenge
                    challenge = it.let { removeUserScoreFromChallenge(it, currentUser) }
                    scoreOfCurrentUser = it.participants.getOrDefault(currentUser, 0)
                    isVoting = true
                }
            }
            isLoading = false
        } else if (isVoting && challengeVote != null) {
            isVoting = false
            hasVoted = true
        } else if (hasVoted) {
            coroutineScope.launch {
                challengeAfterVote = challenge?.copy()
                // update challenge with new score
                challengeVote?.let { challengeVote ->
                    for (participant in challengeVote.participants) {
                        challengeAfterVote = changeParticipantScore(challengeAfterVote!!, participant.key, participant.value)
                    }
                    challengeAfterVote = addParticipant(challengeAfterVote!!, currentUser)
                    challengeAfterVote = changeParticipantScore(challengeAfterVote!!, currentUser, scoreOfCurrentUser)
                }

                // update that current user has voted
                challengeAfterVote = challengeAfterVote?.let {
                    it.copy(participantIsVoted = it.participantIsVoted + (currentUser to true))
                }

                withContext(context = coroutineScope.coroutineContext) {
                    challengeAfterVote?.let { it ->
                        service.updateChallenge(
                            challengeId,
                            it
                        )
                    }
                }
                hasVoted = false
            }
        }
    }

    when {
        isLoading -> LoadingScreen()
        isVoting -> challenge?.let {
            VotingScreen(challenge = it,
                onVoteChanged = { challengeRet ->
                    challengeVote = challengeRet
                },
                onCancelClick = onBack
            )
        }
        hasVoted -> LoadingScreen() // This screen will be shown for a short time before it switches to the RankingScreen
        alreadyVoted -> challenge?.let { // If the user has already voted, render the RankingScreen with the original challenge
            RankingScreen(
                challenge = it,
                onBack = onBack,
            )
        }
        else -> challengeAfterVote?.let {
            RankingScreen(
                challenge = it,
                onBack = onBack,
            )
        }
    }
}

fun removeUserScoreFromChallenge(challenge: Challenge, currentUser: String): Challenge {
    return challenge.copy(
        participants = challenge.participants - currentUser,
        participantIsVoted = challenge.participantIsVoted - currentUser
    )
}
