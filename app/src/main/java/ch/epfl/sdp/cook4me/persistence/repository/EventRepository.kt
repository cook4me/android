package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.ui.event.form.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "events"

class EventRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) : BaseRepository(store,
    COLLECTION_PATH) {
    /*

    * Retrieves event with query name at the given field
    * e.g. getWithGivenField("name", "darth.vadar") will return a map of events with name (event attr.) "darth.vadar"
    * map id: the id of the event, map value: the event object
    * When nothing is found, an empty map is returned
    * */
    suspend fun getWithGivenField(field: String, query: Any): Map<String, Event> {
        val result = store.collection(COLLECTION_PATH).whereEqualTo(field, query).get().await()
        return result.documents.associate { it.id to documentSnapshotToEvent(it) }
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
        val result = store.collection(COLLECTION_PATH).document(id).get().await()
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