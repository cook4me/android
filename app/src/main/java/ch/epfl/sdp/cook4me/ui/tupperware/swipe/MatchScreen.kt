package ch.epfl.sdp.cook4me.ui.tupperware.swipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R


@Composable
fun MatchScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pic1),
                    contentDescription = "Profile picture of user 1",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_star_filled),
                    contentDescription = "Match Icon",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 16.dp, end = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.pic2),
                    contentDescription = "Profile picture of user 2",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = "Match Success!",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /* handle chat action */ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trophy),
                        contentDescription = "Chat Button"
                    )
                }

                IconButton(
                    onClick = { /* handle cancel action */ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trophy),
                        contentDescription = "Cancel Button"
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMatchScreen() {
    MatchScreen()
}
