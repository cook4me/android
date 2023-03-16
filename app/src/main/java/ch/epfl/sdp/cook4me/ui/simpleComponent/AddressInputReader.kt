package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

/**
 * A component that asks for an address
 * @param question the question to be displayed
 * @param onAddressChanged the function to be called when the address is changed
 */
@Composable
fun AddressInputReader(
    question: String = "What is the location?",
    onAddressChanged: (String) -> Unit = {}
) {
    val location = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val zipCode = remember { mutableStateOf("") }

    fun fullAddress(): String {
        return location.value + ", " + zipCode.value + " " + city.value
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        // make the text field take the whole space
        TextField(
            value = location.value,
            label = { Text(stringResource(id = R.string.street_address_label)) },
            onValueChange = {
                location.value = it
                onAddressChanged(fullAddress())
            },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth())
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // weight is used to make the text field take 3/4 of the space
            TextField(value = city.value, label = {
                Text(stringResource(id = R.string.city_label))
                onAddressChanged(fullAddress())
            }, onValueChange = {
                city.value = it
                onAddressChanged(fullAddress())
            },
                modifier = androidx.compose.ui.Modifier.weight(3f)
            )
            TextField(value = zipCode.value, label = {
                Text(stringResource(id = R.string.zip_code_label))
                onAddressChanged(fullAddress())
            }, onValueChange = {
                zipCode.value = it
                onAddressChanged(fullAddress())
            },
                modifier = androidx.compose.ui.Modifier.weight(1f)
            )
        }
    }
}