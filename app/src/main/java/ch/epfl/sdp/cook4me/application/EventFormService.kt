package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event

/**
 * Service that handles the submission of the event form
 */
class EventFormService(
    private val eventRepository: EventRepository = EventRepository()
) {

    /**
     * Submits the form if it is valid, otherwise returns the error message
     * @param event the event to submit
     * @return null if the event is valid, the error message otherwise
     */
    suspend fun submitForm(event: Event): String? = if (event.isValidEvent) {
        eventRepository.add(event)
        null
    } else {
        event.eventProblem
    }

    /*
    * To get the event of given id.
    * If nothing is found, null is returned
    * */
    suspend fun getEventWithId(id: String): Event? =
        eventRepository.getById(id)

    /*
    * Retrieve all events in a Map
    * */
    suspend fun retrieveAllEvents(): Map<String, Event> = eventRepository.getAll()
}
