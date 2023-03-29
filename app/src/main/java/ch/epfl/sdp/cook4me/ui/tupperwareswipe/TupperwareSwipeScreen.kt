package ch.epfl.sdp.cook4me.ui.tupperwareswipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R

@Composable
fun TupperwareSwipeScreen() {
    Column {
        Text("Title")
        Image(
            painter = painterResource(id = R.drawable.carbonara),
            contentDescription = "Carbonara",
            modifier = Modifier.fillMaxWidth()
        )
        Text("Some Description")
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(modifier = Modifier.weight(weight = 1f), onClick = {}) {
                Text("nope")
            }
            Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.05f))
            Button(modifier = Modifier.weight(weight = 1f), onClick = {}) {
                Text("yes")
            }
        }
    }
}