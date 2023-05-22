package ch.epfl.sdp.cook4me.ui.challenge.feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.persistence.repository.ChallengeRepository
import ch.epfl.sdp.cook4me.ui.challenge.Challenge

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
    private val repository: ChallengeRepository = ChallengeRepository()
) : ViewModel() {
    private var _isLoading = mutableStateOf(true)
    private val _challenges = mutableStateListOf<Pair<String, Challenge>>()
    private val _viewedChallenges = mutableStateListOf<Pair<String, Challenge>>()
    private val _chosenSortOption = mutableStateOf<String>("By Name")
    private val _selectedFilters = mutableStateListOf<String>()

    val selectedFilters = mutableStateListOf<String>()
    val chosenSortOption = mutableStateOf(_chosenSortOption.value)
    val filters = mapOf(
        "Food Types" to foodTypeOptions
    )
    val sortOptions = listOf(
        "By Name",
        "By Date",
        "By Participant Count",
    )

    val isLoading: State<Boolean> = _isLoading
    val challenges = _viewedChallenges
    val query = mutableStateOf("")

    fun selectFilterOption(option: String) {
        if (selectedFilters.contains(option)) {
            selectedFilters.remove(option)
        } else {
            selectedFilters.add(option)
        }
    }

    fun cancelChoice() {
        selectedFilters.clear()
        selectedFilters.addAll(_selectedFilters)
        chosenSortOption.value = _chosenSortOption.value
    }

    fun confirmChoice() {
        _selectedFilters.clear()
        _selectedFilters.addAll(selectedFilters)
        _chosenSortOption.value = chosenSortOption.value
    }

    fun resetFilters() {
        selectedFilters.clear()
        chosenSortOption.value = "By Name"
    }

    suspend fun loadNewData() {
        _isLoading.value = true
        _challenges.clear()
        _challenges.addAll(repository.getAll().toList())
        _viewedChallenges.clear()
        filterAndSort()
        _isLoading.value = false
    }

    fun search() {
        val filteredChallenges = _challenges.filter {
            it.second.name.contains(query.value, ignoreCase = true) or
                it.second.description.contains(query.value, ignoreCase = true)
        }
        _viewedChallenges.clear()
        _viewedChallenges.addAll(
            filteredChallenges
        )
    }

    private fun filterAndSort() {
        val filteredChallenges =
            if (_selectedFilters.isNotEmpty()) {
                _challenges.filter { _selectedFilters.contains(it.second.type) }
            } else {
                _challenges
            }
        _viewedChallenges.clear()
        _viewedChallenges.addAll(sort(filteredChallenges))
    }

    private fun sort(list: List<Pair<String, Challenge>>): List<Pair<String, Challenge>> {
        val ret = when (_chosenSortOption.value) {
            "By Name" -> list.sortedBy { it.second.name }
            "By Date" -> list.sortedBy { it.second.dateTime.time.time }
            "By Participant Count" -> list.sortedBy { it.second.participants.size }
            else -> list
        }
        return ret
    }
}
