package ch.epfl.sdp.cook4me.ui.challenge.feed

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import ch.epfl.sdp.cook4me.ui.common.button.AddButton
import ch.epfl.sdp.cook4me.ui.common.filters.FilterButton

@Composable
fun ChallengeFeedScreen(
    onCreateNewChallengeClick: () -> Unit = {},
    onChallengeClick: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    searchViewModel: SearchViewModel = remember { SearchViewModel() },
) {
    val query = searchViewModel.query
    val challenges = searchViewModel.challenges
    val isLoading = searchViewModel.isLoading

    LaunchedEffect(Unit) {
        searchViewModel.loadNewData()
        Log.d("Challenge", "Load new data")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 20.dp,
            ) {
                Column(Modifier.padding(10.dp)) {
                    SearchBar(
                        modifier = Modifier,
                        text = query.value,
                        onTextChange = { query.value = it },
                        onSearch = { searchViewModel.search() },
                        textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    FilterButton(modifier = Modifier.fillMaxWidth(), onClick = onFilterClick)
                }
            }
            if (isLoading.value) {
                LoadingScreen()
            } else {
                Spacer(modifier = Modifier.size(5.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
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
        AddButton(modifier = Modifier.padding(16.dp), onClick = onCreateNewChallengeClick)
    }
}
