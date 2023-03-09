package ch.epfl.sdp.cook4me.ui.simpleComponent

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun TimePickerComponent(
    onTimeChanged: (Calendar) -> Unit
) {
    // code taken from https://www.geeksforgeeks.org/time-picker-in-android-using-jetpack-compose/
    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }

    // Creating a TimePicker dialog
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
            onTimeChanged(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, mHour)
                set(Calendar.MINUTE, mMinute)
            })
        }, mHour, mMinute, false
    )

    Row(verticalAlignment = Alignment.CenterVertically) {

        // Display selected time
        Text(text = "Selected Time: ${mTime.value}")

        Spacer(modifier = Modifier.size(8.dp))

        // On button click, TimePicker is
        // displayed, user can select a time
        Button(onClick = { mTimePickerDialog.show() }) {
            Text(text = "Select time")
        }

    }
}