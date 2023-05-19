package ch.epfl.sdp.cook4me.ui.challengefeed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.epfl.sdp.cook4me.application.ChallengeFormService
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import kotlinx.coroutines.launch

val foodTypeOptions = listOf(
    "African",
    "American",
    "Asian",
    "British",
    "Cajun",
    "Caribbean",
    "Chinese",
    "Eastern European",
    "French",
    "German",
    "Greek",
    "Indian",
    "Irish",
    "Italian",
    "Japanese",
    "Korean",
    "Mediterranean",
    "Mexican",
    "Middle Eastern",
    "Nordic",
    "Southern",
    "Spanish",
    "Swiss",
    "Thai",
    "Vietnamese"
)

class SearchViewModel(
    private val repository: ChallengeFormService = ChallengeFormService()
) : ViewModel() {
    private var _isLoading = mutableStateOf(true)
    private val _challenges = mutableStateListOf<Pair<String, Challenge>>()
    private val _viewedChallenges = mutableStateListOf<Pair<String, Challenge>>()
    val chosenSortOption = mutableStateOf<String?>("By Name")
    val selectedFilters = mutableStateListOf<String>() // TODO change to a better way
    val filters = mapOf(
        "Food Types" to foodTypeOptions
    )
    val sortOptions = mapOf<String, (Challenge) -> Comparable<*>>(
        "By Name" to { it.name },
        "By Date" to { it.dateTime.time.time },
        "By Participant Count" to { it.participants.size }
    )

    val isLoading: State<Boolean> = _isLoading
    val challenges = _viewedChallenges
    val query = mutableStateOf("")

    suspend fun loadNewData() {
        _isLoading.value = true
        _challenges.clear()
        _challenges.addAll(repository.retrieveAllChallenges().toList())
        _viewedChallenges.clear()
        filterAndSort()
        _isLoading.value = false
    }

    fun reset() {
        viewModelScope.launch {
            _isLoading.value = true
            _challenges.clear()
            _viewedChallenges.clear()
            _challenges.addAll(repository.retrieveAllChallenges().toList())
            _viewedChallenges.addAll(_challenges)
            _isLoading.value = false
        }
    }

    fun resetFilters() {
        chosenSortOption.value = "By Name"
        selectedFilters.clear()
    }

    fun filterAndSort() {
        val filteredChallenges =
            if (selectedFilters.isNotEmpty()) {
                _challenges.filter { selectedFilters.contains(it.second.type) }
            } else {
                _challenges
            }
        _viewedChallenges.clear()
        _viewedChallenges.addAll(sort(filteredChallenges))
    }

    fun search() {
        Log.d("SearchViewModel", "Searching")
        val filteredChallenges = _challenges.filter {
            it.second.name.contains(query.value, ignoreCase = true) or
                it.second.description.contains(query.value, ignoreCase = true)
        }
        _viewedChallenges.clear()
        _viewedChallenges.addAll(
            filteredChallenges
        )
        Log.d("SearchViewModel", "${query.value} filtered: ${_challenges.size}")
    }

    private fun sort(list: List<Pair<String, Challenge>>) =
        when (chosenSortOption.value) {
            "By Name" -> list.sortedBy { it.second.name }
            "By Date" -> list.sortedBy { it.second.dateTime.time.time }
            "By Participant Count" -> list.sortedBy { it.second.participants.size }
            else -> list
        }
}
