package ch.epfl.sdp.cook4me.ui.challenge.vote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import ch.epfl.sdp.cook4me.ui.common.form.FormButtons
import ch.epfl.sdp.cook4me.ui.common.Toolbar

private const val MIN_STAR = 1
private const val MAX_STAR = 5

@Composable
fun VotingScreen(
    challenge: Challenge,
    onVoteChanged: (Challenge) -> Unit,
    onCancelClick: () -> Unit
) {
    val voteResults = remember {
        mutableStateMapOf<String, Int>().also { map ->
            challenge.participants.keys.forEach { participant ->
                map[participant] = map[participant] ?: 0
            }
        }
    }

    Column {
        Toolbar(stringResource(R.string.voteScreenTitle))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(challenge.participants.toList()) { (participant) ->
                ParticipantRow(participant, voteResults[participant] ?: 0) { newScore ->
                    voteResults[participant] = newScore
                }
            }
        }

        FormButtons(
            onCancelText = R.string.ButtonRowCancel,
            onSaveText = R.string.voteButton,
            onCancelClick = onCancelClick,
            onSaveClick = {
                val updatedChallenge = challenge.copy(
                    participants = voteResults.mapValues { it.value }
                )
                onVoteChanged(updatedChallenge)
            },
        )
    }
}

@Composable
fun ParticipantRow(participant: String, score: Int, onScoreChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // fill the remaining space
                .wrapContentWidth(Alignment.Start)
        ) {
            Text(
                text = participant,
                style = MaterialTheme.typography.h6
            )
        }

        Box(
            modifier = Modifier.wrapContentWidth(Alignment.End)
        ) {
            RatingBar(
                value = score,
                participant = participant
            ) { newScore ->
                onScoreChange(newScore)
            }
        }
    }
}

@Composable
fun RatingBar(participant: String, value: Int, onValueChange: (Int) -> Unit) {
    var selectedValue by remember { mutableStateOf(value) }
    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 1.dp)
        ) {
            for (i in MIN_STAR..MAX_STAR) {
                IconButton(
                    onClick = {
                        if (selectedValue == i) {
                            selectedValue = 0
                        } else {
                            selectedValue = i
                        }
                        onValueChange(selectedValue)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id =
                            if (i <= selectedValue) {
                                R.drawable.ic_star_filled
                            } else {
                                R.drawable.ic_star_empty
                            }
                        ),
                        contentDescription =
                        if (i <= selectedValue) {
                            "$participant Star $i"
                        } else {
                            "$participant Empty star $i"
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
