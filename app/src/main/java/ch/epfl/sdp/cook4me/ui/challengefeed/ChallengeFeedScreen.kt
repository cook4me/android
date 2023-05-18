package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challengefeed.ChallengeItem
import ch.epfl.sdp.cook4me.ui.challengefeed.SearchBar
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton
import java.util.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.primarySurface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challengefeed.FilterButton
import ch.epfl.sdp.cook4me.ui.challengefeed.FilterScreen
import ch.epfl.sdp.cook4me.ui.challengefeed.SearchViewModel
import com.google.firebase.firestore.GeoPoint


val cookingChallengeList = listOf(
    Challenge(
        name = "Baking Challenge",
        description = "Bake a delicious cake",
        dateTime = Calendar.getInstance().apply { set(2023, Calendar.MAY, 20, 9, 0) },
        latLng = GeoPoint(0.0, 0.0),
        participants = mapOf(
            "User1" to 5,
            "User2" to 7,
            "User3" to 3
        ),
        participantIsVoted = mapOf(
            "User1" to true,
            "User2" to false,
            "User3" to true
        ),
        creator = "Admin",
        type = "Baking"
    ),
    Challenge(
        name = "Grilling Challenge",
        description = "Prepare a mouthwatering barbecue",
        dateTime = Calendar.getInstance().apply { set(2023, Calendar.JUNE, 1, 12, 0) },
        latLng = GeoPoint(0.0, 0.0),
        participants = mapOf(
            "User4" to 10,
            "User5" to 8,
            "User6" to 6
        ),
        participantIsVoted = mapOf(
            "User4" to true,
            "User5" to true,
            "User6" to false
        ),
        creator = "Admin",
        type = "Grilling"
    ),
    Challenge(
        name = "Dessert Challenge",
        description = "Create a delectable dessert",
        dateTime = Calendar.getInstance().apply { set(2023, Calendar.MAY, 25, 18, 30) },
        latLng = GeoPoint(0.0, 0.0),
        participants = mapOf(
            "User7" to 2,
            "User8" to 4,
            "User9" to 6
        ),
        participantIsVoted = mapOf(
            "User7" to false,
            "User8" to true,
            "User9" to true
        ),
        creator = "Admin",
        type = "Dessert"
    )
)

@Composable
fun ChallengeFeedScreen(
    onCreateNewChallengeClick: () -> Unit = {},
    onChallengeClick: (String) -> Unit = {},
    searchViewModel: SearchViewModel = remember { SearchViewModel()},
) {
    val query = searchViewModel.query
    val challenges = searchViewModel.challenges
    val isLoading = searchViewModel.isLoading

    Column(
        modifier = Modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 20.dp,
        ) {
            Column (Modifier.padding(10.dp)) {
                SearchBar(
                    modifier = Modifier,
                    text = query.value,
                    onTextChange = { query.value = it },
                    onSearch = { searchViewModel.search() },
                    textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
                )
                Spacer(modifier = Modifier.size(8.dp))
                FilterButton(modifier = Modifier.fillMaxWidth(), onClick = {})
            }
        }
        if (!isLoading.value) {
            Spacer(modifier = Modifier.size(5.dp))
            LazyColumn() {
                items(challenges) {
                    ChallengeItem(
                        challengeName = it.second.name,
                        creatorName = it.second.creator,
                        participantCount = it.second.participants.size,
                        onClick = { onChallengeClick(it.first) }
                    )
                }
            }
        }
    }
}


@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Button"
        )
    }
}