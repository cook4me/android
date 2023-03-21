package ch.epfl.sdp.cook4me.ui.simpleComponent

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import java.util.Calendar

/**
 * A simple component that allows the user to select a time (hour and minute)
 * @param onTimeChanged the function to be called when the time is changed
 */
@Composable
fun TimePickerComponent(
    onTimeChanged: (Calendar) -> Unit
) {
    // code taken from https://www.geeksforgeeks.org/time-picker-in-android-using-jetpack-compose/
    val mContext = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val time = remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        mContext,
        { _, hour_: Int, minute_: Int ->
            time.value = "$hour_:$minute_"
            onTimeChanged(
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour_)
                    set(Calendar.MINUTE, minute_)
                }
            )
        },
        hour, minute, false
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${stringResource(id = R.string.selected_time_text)}${time.value}")
        Spacer(modifier = Modifier.size(8.dp))

        // On button click, TimePicker is
        // displayed, user can select a time
        Button(onClick = { timePickerDialog.show() }) {
            Text(text = stringResource(id = R.string.select_time_button))
        }
    }
}
