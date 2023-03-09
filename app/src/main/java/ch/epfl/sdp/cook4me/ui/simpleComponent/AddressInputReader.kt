package ch.epfl.sdp.cook4me.ui.simpleComponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp


@Composable
fun AddressInputReader(
    question: String = "What is the location?",
//    onAddressChanged : (Array<String>) -> Unit = {}
){
    val address = remember { Array(3){" "} }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question)
        // make the text field take the whole space
        TextField(value ="", label = { Text("Street address") }, onValueChange = {address[0] = it}
            , modifier = androidx.compose.ui.Modifier.fillMaxWidth())
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            // weight is used to make the text field take 3/4 of the space
            TextField(value ="", label = { Text("City") }, onValueChange = {address[1] = it},
                modifier = androidx.compose.ui.Modifier.weight(3f))
            TextField(value ="", label = { Text("Zip code") }, onValueChange = {address[2] = it},
                modifier = androidx.compose.ui.Modifier.weight(1f))
        }
    }
}