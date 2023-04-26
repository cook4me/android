package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.ui.event.form.Event

/**
 * Service that handles the submission of the event form
 */
class EventFormService(private val repository: EventRepository = EventRepository()) {

    /**
     * Submits the form if it is valid, otherwise returns the error message
     * @param event the event to submit
     * @return null if the event is valid, the error message otherwise
     */
    suspend fun submitForm(event: Event): String? = if (event.isValidEvent) {
        repository.add(event)
        null
    } else {
        event.eventProblem
    }

}
