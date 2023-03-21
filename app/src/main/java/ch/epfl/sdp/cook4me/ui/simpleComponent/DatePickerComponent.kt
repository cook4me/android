package ch.epfl.sdp.cook4me.ui.simpleComponent

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
 * A component that allows the user to select a date
 * @param initialDate the initial date to be displayed
 * @param onDateChange the function to be called when the date is selected
 * @param modifier the modifier of the component
 */
@Composable
fun DatePickerComponent(
    initialDate: Calendar,
    onDateChange: (Calendar) -> Unit,
    modifier: Modifier = Modifier,
) {
    // code inspired from https://www.geeksforgeeks.org/date-picker-in-android-using-jetpack-compose/
    val date = remember { mutableStateOf("") }

    val year = initialDate.get(Calendar.YEAR)
    val month = initialDate.get(Calendar.MONTH)
    val day = initialDate.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year_: Int, month_: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/${month_ + 1}/$year_"
            onDateChange(
                Calendar.getInstance().apply {
                    set(year_, month_, dayOfMonth)
                }
            )
        },
        year, month, day
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(id = R.string.selected_date_text)}${date.value}"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                datePickerDialog.show()
            }
        ) {
            Text(text = stringResource(id = R.string.select_date_button))
        }
    }
}
