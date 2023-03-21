package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * A simple component that displays a question and a toggle button
 * @param question the question to be displayed
 * @param answerChecked the text to be displayed when the toggle button is checked (default)
 * @param answerUnchecked the text to be displayed when the toggle button is unchecked
 * @param onToggle the function to be called when the toggle button is changed
 */
@Composable
fun ToggleButtonChoice(
    question: String,
    answerChecked: String,
    answerUnchecked: String,
    onToggle: (String) -> Unit,
) {
    val checked = remember { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = checked.value,
                onCheckedChange =
                {
                    checked.value = it
                    if (it) {
                        onToggle(answerChecked)
                    } else {
                        onToggle(answerUnchecked)
                    }
                },
                modifier = Modifier.testTag("switch")
            )
            Text(if (checked.value) answerChecked else answerUnchecked)
        }
    }
}
