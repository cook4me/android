package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "tupperwares"

class TupperwareRepository(
    private val store: FirebaseFirestore
) {

    suspend fun add(value: Tupperware) {
        store.collection(COLLECTION_PATH).add(value).await()
    }

    suspend fun getAll(): Map<String, Tupperware> {
        val result = store.collection(COLLECTION_PATH).get().await()
        return result.map { it.id }.zip(result.toObjects(Tupperware::class.java)).toMap()
    }

    suspend fun getById(id: String) =
        store.collection(COLLECTION_PATH).document(id).get().await()
            .toObject(Tupperware::class.java)

    suspend fun update(id: String, value: Tupperware) {
        store.collection(COLLECTION_PATH).document(id).set(value).await()
    }
}
