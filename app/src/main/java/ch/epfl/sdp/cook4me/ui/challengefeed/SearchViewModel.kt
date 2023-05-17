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


class SearchViewModel(
    repository: ChallengeFormService = ChallengeFormService()
): ViewModel() {
    private var _isLoading = mutableStateOf(true)
    private val _challenges = mutableStateListOf<Challenge>()
    private val _viewedChallenges = mutableStateListOf<Challenge>()
    private val _filter = Filter<Challenge>()

    val isLoading: State<Boolean> = _isLoading
    val challenges = _viewedChallenges
    val query = mutableStateOf("")

    init {
        viewModelScope.launch {
            _isLoading.value = true
            _challenges.addAll(repository.retrieveAllChallenges().map{ it.value })
            _viewedChallenges.addAll(_challenges)
            Log.d("SearchViewModel", "Retrieved")
            _isLoading.value = false
        }
    }

    fun search() {
        Log.d("SearchViewModel", "Searching")
        val filteredChallenges = _challenges.filter {
            it.name.contains(query.value, ignoreCase = true) or
            it.description.contains(query.value, ignoreCase = true)
        }
        _viewedChallenges.clear()
        _viewedChallenges.addAll(
            filteredChallenges
        )
        Log.d("SearchViewModel", "${query.value} filtered: ${_challenges.size}")
    }

    fun filter() {

    }


}