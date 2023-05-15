package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge

const val INCREMENT = 1
const val MAX = 3
const val WIDTH_SCORE = 0.7f
const val WIDTH_PARTICIPANT = 3f
const val WIDTH_PLACE = 1f

@Composable
fun RankingScreen(challenge: Challenge, onBack: () -> Unit) {
    val sortedParticipants = challenge.participants.toList().sortedByDescending { it.second }

    Column {
        BasicToolbar(stringResource(R.string.rankingScreenTitle))

        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Place",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(WIDTH_PLACE)
                    )
                    Text(
                        text = "Participant",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(WIDTH_PARTICIPANT)
                    )
                    Text(
                        text = "Score",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(WIDTH_SCORE)
                    )
                }
            }

            itemsIndexed(sortedParticipants) { index, pair ->
                ParticipantScoreRow(
                    index,
                    pair.first,
                    pair.second,
                    pair.first == sortedParticipants[0].first
                )
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun ParticipantScoreRow(index: Int, participant: String, score: Int, isWinner: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (index < MAX) "${index + INCREMENT}." else "",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .weight(1f)
                .testTag("place $index")
        )

        Row(
            modifier = Modifier.weight(WIDTH_PARTICIPANT)
        ) {
            Text(
                text = "$participant ",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            if (isWinner) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trophy),
                    contentDescription = "Winner",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 1.dp)
                )
            }
        }

        Text(
            text = score.toString(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(WIDTH_SCORE)
        )
    }
}

@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = toolbarColor()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
