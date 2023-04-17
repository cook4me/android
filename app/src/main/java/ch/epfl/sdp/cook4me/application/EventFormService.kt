package ch.epfl.sdp.cook4me.application

import android.util.Log
import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.eventform.Event
import com.google.firebase.firestore.DocumentSnapshot

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


    /*
    * Retrieves event with query name at the given field
    * e.g. getWithGivenField("name", "darth.vadar") will return the event with name (event attr.) "darth.vadar"
    * When nothing is found, an empty map is returned
    * */
    suspend fun getWithGivenField(field: String, query: String) : Map<String, Event> {
        val result = objectRepository.getWithGivenField<Event>(field, query)
        return result.map { it.id to documentSnapshotToEvent(it) }.toMap()
    }

    /*
    * To get the first event of queried map of events.
    * If nothing is found, null is returned
    * */
    suspend fun getFirstEventWithGivenField(field: String, query:String):Event? {
        val resultMap: Map<String, Event> = getWithGivenField(field, query)
        return resultMap.values.firstOrNull()
    }

    /*
    * Notes: Firebase could not serialize to java.untl.Calender, I will add an constructor in Event.kt
    * to construct an Event object from a map.
    * This function is used to convert a document snapshot to an event object manually.
    * Also see: Event.kt
    * */
    private fun documentSnapshotToEvent(documentSnapshot: DocumentSnapshot): Event {
        return Event(documentSnapshot.data?: emptyMap())
    }
}
