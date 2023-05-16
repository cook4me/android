package ch.epfl.sdp.cook4me.ui.challengedetailed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState

@Suppress("MagicNumber")
@Composable
fun ChallengeDetailedScreen(
    challengeViewModel: ChallengeDetailedViewModel = viewModel(),
    challengeId: String
) {
    val challenge by challengeViewModel.challenge

    LaunchedEffect(challengeId) {
        challengeViewModel.fetchChallenge(challengeId)
    }

    challenge?.let {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = it.name, style = MaterialTheme.typography.h4)

                Spacer(Modifier.height(8.dp))

                Text(text = it.description, style = MaterialTheme.typography.body1)

                Spacer(Modifier.height(8.dp))

                Text(text = "Date: ${it.challengeDate}", style = MaterialTheme.typography.body2)

                Spacer(Modifier.height(8.dp))

                CountdownTimer(it.dateTime)

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.People,
                        contentDescription = "Participants",
                        Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Column {
                        it.participants.forEach { (name, score) ->
                            Row(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.body2,
                                        color = Color.Black // Modify the color as needed
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color.Cyan, shape = RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = score.toString(),
                                        style = MaterialTheme.typography.body2,
                                        color = Color.Black // Modify the color as needed
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(text = "Creator: ${it.creator}", style = MaterialTheme.typography.body2)

                Spacer(Modifier.height(8.dp))

                Text(text = "Type: ${it.type}", style = MaterialTheme.typography.body2)

                Spacer(Modifier.height(8.dp))

                val position = LatLng(it.latLng.first, it.latLng.second)
                GoogleMap(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    cameraPositionState = CameraPositionState(
                        CameraPosition.fromLatLngZoom(position, 15f)
                    )
                ) {
                    val markerState = MarkerState(position = position)
                    MarkerInfoWindow(state = markerState, title = it.name)
                }
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9.toFloat())
                    .height(90.dp)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(R.string.join), style = MaterialTheme.typography.button)
            }
        }
    }
}
