package ch.epfl.sdp.cook4me.ui.common.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * A simple component that displays a question and a toggle button
 * @param question the question to be displayed
 * @param answerChecked the text to be displayed when the toggle button is checked (default)
 * @param answerUnchecked the text to be displayed when the toggle button is unchecked
 * @param onToggle the function to be called when the toggle button is changed (default is true)
 */
@Composable
fun ToggleSwitch(
    @StringRes question: Int,
    @StringRes answerChecked: Int,
    @StringRes answerUnchecked: Int,
    onToggle: (Boolean) -> Unit,
) {
    val checked = remember { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(question))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = checked.value,
                onCheckedChange =
                {
                    checked.value = it
                    onToggle(it)
                },
                modifier = Modifier.testTag("switch"),
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
            )
            Text(
                if (checked.value) {
                    stringResource(answerChecked)
                } else {
                    stringResource(
                        answerUnchecked
                    )
                }
            )
        }
    }
}
