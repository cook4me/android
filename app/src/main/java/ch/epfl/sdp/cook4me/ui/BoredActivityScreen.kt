package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@Composable
fun BoredActivityScreen(
    modifier: Modifier = Modifier,
    onGenerateButtonClicked: () -> Unit,
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier.size(300.dp))
        Text(
            text = stringResource(R.string.bored_screen_default_text),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            //modifier = modifier.fillMaxHeight()

        )
        Spacer(modifier = modifier.size(300.dp))
        Button(
            onClick = { onGenerateButtonClicked() }
        ) {
            Text(stringResource(R.string.bored_screen_generate))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewBoredActivityScreen() {
    BoredActivityScreen(onGenerateButtonClicked = {})
}