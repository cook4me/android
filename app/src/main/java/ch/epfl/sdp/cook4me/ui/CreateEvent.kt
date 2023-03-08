package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.simpleComponent.AddressInputReader
import ch.epfl.sdp.cook4me.ui.simpleComponent.InputTextReader

@Composable
fun CreateEvent() {
    Column (
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        InputTextReader(question = "What is the location?", onTextChanged = {})
        AddressInputReader()
    }
}