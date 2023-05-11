package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun CountdownTimer(dateTime: Calendar) {
    val currentTime = remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(key1 = "timer") {
        while (true) {
            delay(1000L)
            currentTime.value = Calendar.getInstance()
        }
    }

    val timeDiff = dateTime.timeInMillis - currentTime.value.timeInMillis
    if (timeDiff > 0) {
        val hours = timeDiff / (1000 * 60 * 60)
        val minutes = (timeDiff % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (timeDiff % (1000 * 60)) / 1000

        Text(text = "Time remaining: ", style = MaterialTheme.typography.body2)
        Text(text = "${hours}h ${minutes}m ${seconds}s", style = MaterialTheme.typography.h5)
    } else {
        Text(text = "This event has already taken place", color = Color.Red, style = MaterialTheme.typography.body2)
    }
}
