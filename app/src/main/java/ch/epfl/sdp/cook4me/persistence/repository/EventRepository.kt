package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.ui.event.form.Event
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val COLLECTION_PATH = "events"

class EventRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    suspend fun add(event: Event) =
        store.addObjectToCollection(event, COLLECTION_PATH)

    suspend fun getById(id: String) = store.getObjectByIdFromCollection(id, COLLECTION_PATH, ::transformToEvent)

    suspend fun getAll() = store.getAllObjectsFromCollection(COLLECTION_PATH, ::transformToEvent)

    suspend fun deleteAll() = store.deleteAllDocumentsFromCollection(COLLECTION_PATH)

    private fun transformToEvent(snapshot: DocumentSnapshot) = Event(snapshot.data ?: emptyMap())
}
