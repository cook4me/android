package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.EventRepository
import ch.epfl.sdp.cook4me.persistence.repository.ObjectCollectionRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event
import com.google.firebase.firestore.DocumentSnapshot

private const val EVENT_PATH = "events"

/**
 * Service that handles the submission of the event form
 */
class EventFormService(
    private val eventRepository: EventRepository = EventRepository(),
    private val objectCollectionRepository: ObjectCollectionRepository =
        ObjectCollectionRepository(objectPath = EVENT_PATH)
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
    suspend fun getEventWithId(id: String): Event? {
        val result = eventRepository.getWithId<Event>(id)
        return result?.let { documentSnapshotToEvent(it) }
    }

    /*
    * Retrieve all events in a Map
    * */
    suspend fun retrieveAllEvents(): Map<String, Event> {
        val result = objectCollectionRepository.retrieveAllDocuments<Event>()
        return result.map { it.id to documentSnapshotToEvent(it) }.toMap()
    }

    /*
    * Notes: Firebase could not serialize to java.untl.Calender, I will add an constructor in Event.kt
    * to construct an Event object from a map.
    * This function is used to convert a document snapshot to an event object manually.
    * Also see: Event.kt
    * */
    private fun documentSnapshotToEvent(documentSnapshot: DocumentSnapshot) =
        Event(documentSnapshot.data ?: emptyMap())
}
