package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import ch.epfl.sdp.cook4me.ui.user.profile.ProfileScreen

// I had to add this activity solely to show profile from
// the message screen, because I could not pass the
// navcontroller to the MessageActivity.
class ChatProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(EXTRA_PROFILE_ID) ?: ""
        setContent {
            Cook4meTheme() {
                ProfileScreen()
            }
        }
    }

    companion object {
        private const val EXTRA_PROFILE_ID = "ch.epfl.sdp.cook4me.PROFILE_ID"

        fun getIntent(context: Context, profileId: String): Intent =
            Intent(context, ChatProfileActivity::class.java).apply {
                putExtra(EXTRA_PROFILE_ID, profileId)
            }
    }
}
