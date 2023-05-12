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
                val dateTime = Calendar.getInstance()
                // to ensure event is in the future
                dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
                Column {
                    val challenge = Challenge(
                        name = "name",
                        description = "description",
                        dateTime = dateTime,
                        participants = mapOf("participant1" to 0, "participant2" to 0),
                        creator = "darth.vader@epfl.ch",
                        type = "French"
                    )

                    VotingScreen(challenge,  {challenge -> {}})
                }
            }
        }
    }
}
