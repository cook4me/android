package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event

private const val EVENT_PATH = "events"

/**
 * Service that handles the submission of the event form
 */
class EventFormService(private val objectRepository: ObjectRepository = ObjectRepository(objectPath = EVENT_PATH)) {

    /**
     * Submits the form if it is valid, otherwise returns the error message
     * @param event the event to submit
     * @return null if the event is valid, the error message otherwise
     */
    suspend fun submitForm(event: Event): String? = if (event.isValidEvent) {
        objectRepository.add(event)
        null
    } else {
        event.eventProblem
    }

    suspend fun getById(id: String) = objectRepository.getById<Event>(id)
}
