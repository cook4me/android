package ch.epfl.sdp.cook4me.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.ui.event.form.Event
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
class EventFormServiceTest {

    private val mockEventRepository = mockk<EventRepository>(relaxed = true)
    private val eventFormService = EventFormService(mockEventRepository)

    @Test
    fun submitValidEventStoresEvent() = runTest {
        val dateTime = Calendar.getInstance()
        // to ensure event is in the future
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        val event = Event(
            name = "name",
            description = "description",
            dateTime = dateTime,
            maxParticipants = 10,
            id = "id",
            creator = "creator"
        )
        coEvery { mockEventRepository.add(match { event.isValidEvent }) } returns "someId"
        val result = eventFormService.submitForm(event)
        // assert mockObjectRepository.add was called
        coVerify {
            mockEventRepository.add(match { event.isValidEvent })
        }
        assert(result == null)
    }

    @Test
    fun submitIncompleteEventReturnsErrorMessage() = runTest {
        val event = Event()
        val result = eventFormService.submitForm(event)

        // assert mockObjectRepository.add was not called
        coVerify(exactly = 0) {
            mockEventRepository.add(match { event.isValidEvent })
        }
        assert(result != null)
    }
}
