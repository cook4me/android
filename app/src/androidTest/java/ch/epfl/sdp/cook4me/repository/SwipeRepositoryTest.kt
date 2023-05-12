package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.repository.SwipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SwipeRepositoryTest {
    private val swipeRepository: SwipeRepository = SwipeRepository()

    @Test
    fun swipeTest() = runTest {
        swipeRepository.add("someId", false)
    }

    @Test
    fun getAllTest() = runTest {
        val repos = swipeRepository.getAllCreatedByLoginUser()
        println(repos)
    }
}