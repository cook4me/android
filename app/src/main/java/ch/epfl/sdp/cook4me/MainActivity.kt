package ch.epfl.sdp.cook4me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import ch.epfl.sdp.cook4me.ui.challenge.VotingScreen
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cook4meTheme {
                Column {
                    val challenge = Challenge(
                        name = "name",
                        description = "description",
                        dateTime = Calendar.getInstance(),
                        participants = mapOf("participant1" to 0, "participant2" to 0, "participant3" to 0, "participant4" to 0, "participant5" to 0),
                        creator = ""
                    )

                    VotingScreen(
                        challenge = challenge,
                        onVoteChanged ={},
                        onCancelClick = {},
                    )
                }
            }
        }
    }
}
