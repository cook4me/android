package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "profiles"

class ProfileRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun add(value: Profile): String {
        store.collection(COLLECTION_PATH).document(value.email).set(value).await()
        return value.email // return id
    }

    suspend fun getById(id: String) =
        store.getObjectByIdFromCollection<Profile>(id, COLLECTION_PATH)

    suspend fun update(id: String, value: Profile) =
        store.updateObjectInCollection(id, value, COLLECTION_PATH)

    suspend fun deleteAll() {
        store.deleteAllDocumentsFromCollection(COLLECTION_PATH)
    }
}
