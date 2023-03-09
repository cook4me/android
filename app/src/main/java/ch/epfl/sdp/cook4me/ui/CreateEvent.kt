package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.simpleComponent.AddressInputReader
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputTextReader
import ch.epfl.sdp.cook4me.ui.simpleComponent.IntegerSlider
import ch.epfl.sdp.cook4me.ui.simpleComponent.ToggleButtonChoice

@Composable
fun CreateEvent() {
    Column (
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        InputTextReader(question = "What is the location?", onTextChanged = {})
        AddressInputReader()
        IntegerSlider(text = "Number of people to invite", min = 2, max = 16, onValueChange = {}, modifier = Modifier.fillMaxWidth())
        ToggleButtonChoice(question = "Who can see the event", possibilities = Pair("Everyone","Subscriber only"), onToggle = {})
        // submit button
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Submit")
        }
    }
}