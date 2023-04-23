package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.BaseRepository
import ch.epfl.sdp.cook4me.ui.event.form.Event
import com.google.firebase.firestore.DocumentSnapshot

private const val EVENT_PATH = "events"

/**
 * Service that handles the submission of the event form
 */
class EventFormService(private val baseRepository: BaseRepository = BaseRepository(objectPath = EVENT_PATH)) {

    /**
     * Submits the form if it is valid, otherwise returns the error message
     * @param event the event to submit
     * @return null if the event is valid, the error message otherwise
     */
    suspend fun submitForm(event: Event): String? = if (event.isValidEvent) {
        baseRepository.add(event)
        null
    } else {
        event.eventProblem
    }

    /*
    * Retrieves event with query name at the given field
    * e.g. getWithGivenField("name", "darth.vadar") will return a map of events with name (event attr.) "darth.vadar"
    * map id: the id of the event, map value: the event object
    * When nothing is found, an empty map is returned
    * */
    suspend fun getWithGivenField(field: String, query: Any): Map<String, Event> {
        val result = baseRepository.getWithGivenField<Event>(field, query)
        return result.map { it.id to documentSnapshotToEvent(it) }.toMap()
    }

    /*
    * To get the first event of queried map of events.
    * If nothing is found, null is returned
    * */
    suspend fun getFirstEventWithGivenField(field: String, query: Any): Event? {
        val resultMap: Map<String, Event> = getWithGivenField(field, query)
        return resultMap.values.firstOrNull()
    }

    /*
    * To get the event of given id.
    * If nothing is found, null is returned
    * */
    suspend fun getEventWithId(id: String): Event? {
        val result = baseRepository.getWithId<Event>(id)
        return result?.let { documentSnapshotToEvent(it) }
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
