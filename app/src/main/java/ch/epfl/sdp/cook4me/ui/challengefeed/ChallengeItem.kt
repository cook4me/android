package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge

@Composable
fun ChallengeItem(
    modifier: Modifier = Modifier,
    challengeName: String,
    creatorName: String,
    participantCount: Int,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = challengeName,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "by $creatorName",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Participant Icon",
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    text = "x $participantCount",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 25.dp)
                )
            }
        }
    }
}