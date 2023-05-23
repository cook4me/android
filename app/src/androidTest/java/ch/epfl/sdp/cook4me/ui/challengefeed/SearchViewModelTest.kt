package ch.epfl.sdp.cook4me.ui.challengefeed

import ch.epfl.sdp.cook4me.persistence.repository.ChallengeRepository
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import ch.epfl.sdp.cook4me.ui.challenge.feed.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private val repository = mockk<ChallengeRepository>()

    @Before
    fun setup() {
        coEvery { repository.getAll() } returns mapOf(
            Pair("1", Challenge(name = "Challenge 1", type = "Type 1")),
            Pair("2", Challenge(name = "Challenge 2", type = "Type 2")),
            Pair("3", Challenge(name = "Challenge 3", type = "Type 1")),
        )
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun testLoadNewData() = runTest {
        viewModel.loadNewData()

        // Verify loading status
        assertEquals(false, viewModel.isLoading.value)

        // Verify the challenges are loaded correctly
        assertEquals(3, viewModel.challenges.size)
        assertEquals("Challenge 1", viewModel.challenges[0].second.name)
        assertEquals("Challenge 2", viewModel.challenges[1].second.name)
        assertEquals("Challenge 3", viewModel.challenges[2].second.name)
    }

    @Test
    fun testLoadNewDataOutputs() = runTest {
        viewModel.selectedFilters.addAll(listOf("Type 1"))
        viewModel.chosenSortOption.value = "By Name"
        viewModel.confirmChoice()

        viewModel.loadNewData()

        // Verify loading status
        assertEquals(false, viewModel.isLoading.value)

        // Verify the challenges are loaded, filtered, and sorted correctly
        assertEquals(2, viewModel.challenges.size)
        assertEquals("Challenge 1", viewModel.challenges[0].second.name)
        assertEquals("Challenge 3", viewModel.challenges[1].second.name)
    }

    @Test
    fun testSearch() = runTest {
        viewModel.loadNewData()
        viewModel.query.value = "Challenge 1"
        viewModel.search()

        // Verify the search result
        assertEquals(1, viewModel.challenges.size)
        assertEquals("Challenge 1", viewModel.challenges[0].second.name)
    }

    @Test
    fun testSelectFilterOption() {
        viewModel.selectFilterOption("Type 1")
        assertEquals(1, viewModel.selectedFilters.size)
        assertEquals("Type 1", viewModel.selectedFilters[0])

        viewModel.selectFilterOption("Type 1") // remove selected filter
        assertEquals(0, viewModel.selectedFilters.size)
    }

    @Test
    fun testCancelChoice() {
        viewModel.selectFilterOption("Type 1")
        viewModel.cancelChoice()
        assertEquals(0, viewModel.selectedFilters.size)
        assertEquals("By Name", viewModel.chosenSortOption.value)
    }

    @Test
    fun resetFiltersClearsFiltersAndRevertsToDefaultSort() {
        viewModel.selectFilterOption("Type 1")
        viewModel.chosenSortOption.value = "By Date"
        viewModel.confirmChoice()

        viewModel.resetFilters()

        assertEquals(0, viewModel.selectedFilters.size)
        assertEquals("By Name", viewModel.chosenSortOption.value)
    }
}
