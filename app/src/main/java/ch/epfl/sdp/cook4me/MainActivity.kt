package ch.epfl.sdp.cook4me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ch.epfl.sdp.cook4me.ui.theme.cook4meTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            cook4meTheme {
                cook4MeApp()
            }
        }
    }
}
