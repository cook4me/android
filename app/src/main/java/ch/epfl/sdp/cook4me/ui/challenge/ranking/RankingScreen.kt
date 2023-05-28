package ch.epfl.sdp.cook4me.ui.challenge.ranking

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import ch.epfl.sdp.cook4me.ui.common.Toolbar

private const val INCREMENT = 1
private const val MAX = 3
private const val WIDTH_SCORE = 0.8f
private const val WIDTH_PARTICIPANT = 3f
private const val WIDTH_PLACE = 1f

@Composable
fun RankingScreen(
    challenge: Challenge,
    onBack: () -> Unit,
) {
    val sortedParticipants = challenge.participants.toList().sortedByDescending { it.second }

    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(stringResource(R.string.rankingScreenTitle))

        LazyColumn(
            modifier = Modifier
                .weight(1f) // This ensures the list takes up all available space between the title and button
                .fillMaxWidth()
        ) {
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
                        text = stringResource(R.string.place),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(WIDTH_PLACE)
                    )
                    Text(
                        text = stringResource(R.string.participant),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(WIDTH_PARTICIPANT)
                    )
                    Text(
                        text = stringResource(R.string.score),
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
            Text(stringResource(R.string.back))
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
                    contentDescription = stringResource(R.string.winner),
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
