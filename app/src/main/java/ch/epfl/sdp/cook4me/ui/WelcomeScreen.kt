package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStartButtonClicked: (String) -> Unit,
    onMapButtonClicked: () -> Unit,
) {
    var nameTextField by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(R.string.welcome_message), style = MaterialTheme.typography.h4)
        TextField(placeholder = {
            Text(stringResource(R.string.welcome_screen_name_field))
        }, value = nameTextField, onValueChange = { nameTextField = it })
        Button(onClick = { onStartButtonClicked(nameTextField) }) {
            Text(stringResource(R.string.welcome_screen_button))
        }
        Button(onClick = { onMapButtonClicked() }) {
            Text(stringResource(R.string.welcome_screen_map_button))
        }
    }
}