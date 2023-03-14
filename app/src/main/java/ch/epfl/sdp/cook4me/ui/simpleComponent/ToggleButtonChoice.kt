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
import androidx.compose.ui.unit.dp

/**
 * A simple component that displays a question and a toggle button
 * @param question the question to be displayed
 * @param possibilities the two possible answers to the question
 * @param onToggle the function to be called when the toggle button is changed
 */
@Composable
fun ToggleButtonChoice(
    question: String,
    possibilities: Pair<String, String>,
    onToggle: (String) -> Unit,
) {
    val selected = remember { mutableStateOf(true)}
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
                ){
            Switch(checked = selected.value, onCheckedChange =
                {
                    selected.value = it
                    if (it) {
                        onToggle(possibilities.first)
                    } else {
                        onToggle(possibilities.second)
                    }
                })
            Text(if (selected.value) possibilities.first else possibilities.second)
        }
    }
}