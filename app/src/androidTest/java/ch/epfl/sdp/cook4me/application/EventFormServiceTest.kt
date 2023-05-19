package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import java.util.Calendar

class EventFormServiceTest {

    private val mockObjectRepository = mockk<ObjectRepository>(relaxed = true)

    private val eventFormService = EventFormService(mockObjectRepository)

    @Test
    fun submitValidEventStoresEvent() = runBlocking {
        val dateTime = Calendar.getInstance()
        // to ensure event is in the future
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        val event = Event(
            name = "name",
            description = "description",
            dateTime = dateTime,
            location = "location",
            maxParticipants = 10,
            participants = listOf("participant1", "participant2"),
            id = "id",
            isPrivate = true,
            creator = "creator"
        )
        coEvery { mockObjectRepository.add(match { event.isValidEvent }) } returns "someId"
        val result = withTimeout(500L) {
            eventFormService.submitForm(event)
        }
        // assert mockObjectRepository.add was called
        coVerify {
            mockObjectRepository.add(match { event.isValidEvent })
        }
        assert(result == null)
    }

    @Test
    fun submitIncompleteEventReturnsErrorMessage() = runBlocking {
        val event = Event()
        val result = withTimeout(500L) {
            eventFormService.submitForm(event)
        }
        // assert mockObjectRepository.add was not called
        coVerify(exactly = 0) {
            mockObjectRepository.add(match { event.isValidEvent })
        }
        assert(result != null)
    }
}
