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
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun BoredActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: BoredViewModel = viewModel(),

    ) {
    val boredUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier.size(200.dp))
        Text(
            text = boredUiState.activity,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,

        )
        Spacer(modifier = modifier.size(200.dp))
        Button(
            onClick = { viewModel.generateActivity() }
        ) {
            Text(stringResource(R.string.bored_screen_generate))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewBoredActivityScreen() {
    Cook4meTheme {
        BoredActivityScreen()
    }
}