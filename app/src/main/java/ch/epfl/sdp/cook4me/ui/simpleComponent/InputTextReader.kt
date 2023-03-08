package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp

/**
 * A simple component that displays a question and a text field
 * @param question the question to be displayed
 * @param label the label of the text field
 * @param exampleText the example text to be displayed in the text field
 * @param onTextChanged the function to be called when the text field is changed
 */
@Composable
fun InputTextReader(
    question: String,
    label : String = "",
    exampleText: String = "",
    onTextChanged: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        TextField(
            value = exampleText,
            onValueChange = onTextChanged,
            label = { Text(label)},
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        )
    }
}