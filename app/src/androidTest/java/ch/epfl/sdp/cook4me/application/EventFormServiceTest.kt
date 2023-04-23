package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.BaseRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import java.util.Calendar

class EventFormServiceTest {

    private val mockBaseRepository = mockk<BaseRepository>(relaxed = true)

    private val eventFormService = EventFormService(mockBaseRepository)

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
        coEvery { mockBaseRepository.add(match { event.isValidEvent }) } returns Unit
        val result = withTimeout(500L) {
            eventFormService.submitForm(event)
        }
        // assert mockObjectRepository.add was called
        coVerify {
            mockBaseRepository.add(match { event.isValidEvent })
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
            mockBaseRepository.add(match { event.isValidEvent })
        }
        assert(result != null)
    }
}
