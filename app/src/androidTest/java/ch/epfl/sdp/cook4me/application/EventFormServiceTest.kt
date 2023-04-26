package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.ui.event.form.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import java.util.Calendar

class EventFormServiceTest {

    private val mockRepository = mockk<EventRepository>(relaxed = true)
    private val eventFormService = EventFormService(mockRepository)

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
        coEvery { mockRepository.add(match { event.isValidEvent }) } returns Unit
        val result = withTimeout(500L) {
            eventFormService.submitForm(event)
        }
        // assert mockObjectRepository.add was called
        coVerify {
            mockRepository.add(match { event.isValidEvent })
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
            mockRepository.add(match { event.isValidEvent })
        }
        assert(result != null)
    }
}
