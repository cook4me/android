import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import ch.epfl.sdp.cook4me.ui.challenge.ranking.RankingScreen
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RankingScreenWrapper(
    challengeFormService: ChallengeFormService = ChallengeFormService(),
    onBack: () -> Unit,
    id: String,
) {
    var challenge by remember { mutableStateOf<Challenge?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = challengeFormService) {
        challenge = withContext(Dispatchers.IO) {
            challengeFormService.getChallengeWithId(id = id)
        }
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        challenge?.let {
            RankingScreen(challenge = it, onBack = onBack)
        }
    }
}
