package ch.epfl.sdp.cook4me.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.repository.ChallengeRepository
import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ChallengeFormServiceTest {
    private val mockChallengeRepository = mockk<ChallengeRepository>(relaxed = true)
    private val challengeFormService = ChallengeFormService(mockChallengeRepository)

    @Test
    fun submitValidChallengeStoresChallenge() = runTest {
        val dateTime = Calendar.getInstance()
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        val challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = dateTime,
            participants = mapOf("participant1" to 0, "participant2" to 0),
            participantIsVoted = mapOf("participant1" to false, "participant2" to false),
            creator = "creator",
            type = "testType",
        )
        coEvery { mockChallengeRepository.add(match { challenge.isValidChallenge }) } returns "someId"
        val result =
            challengeFormService.submitForm(challenge)
        // assert mockObjectRepository.add was called
        coVerify {
            mockChallengeRepository.add(match { challenge.isValidChallenge })
        }
        assert(result == null)
    }

    @Test
    fun testUpdateChallenge() = runTest {
        val id = "testId"
        val challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = Calendar.getInstance(),
            participants = mapOf("participant1" to 0, "participant2" to 0),
            participantIsVoted = mapOf("participant1" to false, "participant2" to false),
            creator = "creator",
            type = "testType",
        )

        val updatedChallenge = Challenge(
            name = "updatedName",
            description = "updatedDescription",
            dateTime = Calendar.getInstance(),
            participants = mapOf("participant1" to 1, "participant2" to 2),
            participantIsVoted = mapOf("participant1" to true, "participant2" to true),
            creator = "updatedCreator",
            type = "updatedTestType",
        )

        // Mocking the update function to simply return Unit
        coEvery { mockChallengeRepository.update(id, updatedChallenge) } returns Unit

        // Call the function to update the challenge
        challengeFormService.updateChallenge(id, updatedChallenge)

        // Assert that the update function was called with the correct parameters
        coVerify {
            mockChallengeRepository.update(id, updatedChallenge)
        }
    }

    @Test
    fun submitIncompleteChallengeReturnsErrorMessage() = runTest {
        val challenge = Challenge()
        val result = challengeFormService.submitForm(challenge)
        // assert mockObjectRepository.add was not called
        coVerify(exactly = 0) {
            mockChallengeRepository.add(match { challenge.isValidChallenge })
        }
        assert(result != null)
    }
}
