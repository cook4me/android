package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import java.util.Calendar

class ChallengeFormServiceTest {
    private val mockObjectRepository = mockk<ObjectRepository>(relaxed = true)

    private val challengeFormService = ChallengeFormService(mockObjectRepository)

    @Test
    fun submitValidChallengeStoresChallenge() = runBlocking {
        val dateTime = Calendar.getInstance()
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        val challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = dateTime,
            participants = listOf("participant1", "participant2"),
            creator = "creator",
            type = "testType",
        )
        coEvery { mockObjectRepository.add(match { challenge.isValidChallenge }) } returns Unit
        val result = withTimeout(500L) {
            challengeFormService.submitForm(challenge)
        }
        // assert mockObjectRepository.add was called
        coVerify {
            mockObjectRepository.add(match { challenge.isValidChallenge })
        }
        assert(result == null)
    }

    @Test
    fun submitIncompleteChallengeReturnsErrorMessage() = runBlocking {
        val challenge = Challenge()
        val result = withTimeout(500L) {
            challengeFormService.submitForm(challenge)
        }
        // assert mockObjectRepository.add was not called
        coVerify(exactly = 0) {
            mockObjectRepository.add(match { challenge.isValidChallenge })
        }
        assert(result != null)
    }
}
