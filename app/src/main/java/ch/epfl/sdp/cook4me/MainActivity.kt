package ch.epfl.sdp.cook4me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import ch.epfl.sdp.cook4me.ui.BoredActivityScreen
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cook4meTheme {
                BoredActivityScreen()
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            ActivityDatabase::class.java, "activity_database"
        ).build()

    }
}