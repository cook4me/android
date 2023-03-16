package ch.epfl.sdp.cook4me.ui.simpleComponent

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import java.util.*

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
    val mDate = remember { mutableStateOf("") }

    val mYear = initialDate.get(Calendar.YEAR)
    val mMonth = initialDate.get(Calendar.MONTH)
    val mDay = initialDate.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, mYear_: Int, mMonth_: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth_+1}/$mYear_"
            onDateChange(Calendar.getInstance().apply {
                set(mYear_, mMonth_, mDayOfMonth)
            })
        }, mYear, mMonth, mDay
    )

    Row(modifier = modifier,
    verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(id = R.string.selected_date_text)}: ${mDate.value}"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            mDatePickerDialog.show()
        }) {
            Text(text = stringResource(id = R.string.select_date_button))
        }
    }
}