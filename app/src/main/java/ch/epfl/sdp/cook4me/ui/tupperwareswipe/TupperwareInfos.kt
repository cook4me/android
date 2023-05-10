package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.persistence.model.Tupperware

@Composable
fun TupperwareInfos(
    modifier: Modifier = Modifier,
    tupperware: Tupperware = Tupperware("Title", "Description", "User"),
    onNavigateToProfileWithId: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundColor = screenBackgroundColor()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = tupperware.title, style = MaterialTheme.typography.h6)
            Spacer(modifier = modifier.height(8.dp))
            Text(text = tupperware.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "View Profile",
                style = MaterialTheme.typography.body2,
                color = Color.Blue,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToProfileWithId(tupperware.user) }
            )
        }
    }
}

@Composable
private fun screenBackgroundColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.background else MaterialTheme.colors.surface
